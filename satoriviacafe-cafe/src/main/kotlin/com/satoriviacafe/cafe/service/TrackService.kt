package com.satoriviacafe.cafe.service

import com.satoriviacafe.cafe.domain.CafeProduction
import com.satoriviacafe.cafe.domain.CafeTrackLog
import com.satoriviacafe.cafe.mapper.CafeProductionMapper
import com.satoriviacafe.cafe.mapper.CafeTrackLogMapper
import com.satoriviacafe.common.utils.ip.AddressUtils
import jakarta.annotation.PreDestroy
import jakarta.servlet.http.HttpServletRequest
import org.jctools.queues.MpscLinkedQueue
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


/**
 * 跟踪信息服务
 *
 * @author shy
 * @since 2026年04月21日
 */
@Service
class TrackService(
    private val cafeTrackLogMapper: CafeTrackLogMapper,
    private val cafeProductionMapper: CafeProductionMapper,
) {
    companion object {
        private val trackLogQueue = MpscLinkedQueue<CafeTrackLog>()
        private val flushLock = ReentrantLock()
    }


    fun track(code: String, eventName: String, request: HttpServletRequest) {
        val dbInfo =
            cafeProductionMapper.selectCafeProductionList(CafeProduction().apply { prodCode = code }).firstOrNull()
        val cafeTrackLog = CafeTrackLog().apply {
            prodId = dbInfo?.prodId?.toString()
            prodCode = code
            prodName = dbInfo?.prodName ?: ""
            this.eventName = eventName
            ip = request.remoteAddr
            location = AddressUtils.getRealAddressByIP(request.remoteAddr)
            userAgent = request.getHeader("User-Agent")
            deviceType = request.getHeader("Device-Type")
            browser = request.getHeader("Browser")
            os = request.getHeader("OS")
            pageUrl = request.requestURL.toString()
            referrerUrl = request.getHeader("Referer")
        }
        trackLogQueue.offer(cafeTrackLog)
    }

    /**
     * 定时任务，每5秒处理一次队列中的日志
     */
    @Scheduled(fixedDelay = 5000)
    fun processTrackLogs() {
        flushLock.withLock {
            val all = mutableListOf<CafeTrackLog>() // 临时存储所有日志
            while (!trackLogQueue.isEmpty) { // 从队列中取出所有日志
                all.add(trackLogQueue.poll()) // 添加到临时存储中
            }
            all.chunked(500).forEach { // 每500条批量插入
                cafeTrackLogMapper.insertBatchIgnoreCafeTrackLog(it) // 执行批量插入
            }
        }

    }

    /**
     * 在应用关闭前，确保所有日志都被处理
     */
    @PreDestroy
    fun preDestroy() {
        processTrackLogs()
    }
}
