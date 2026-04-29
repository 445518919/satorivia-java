package com.satoriviacafe.cafe.service

import com.satoriviacafe.cafe.domain.CafeTrackLog
import com.satoriviacafe.cafe.mapper.CafeTrackLogMapper
import com.satoriviacafe.common.utils.ip.AddressUtils.getRealAddressByIP
import com.satoriviacafe.common.utils.ip.IpUtils.getIpAddr
import com.satoriviacafe.framework.manager.AsyncManager
import jakarta.annotation.PreDestroy
import jakarta.servlet.http.HttpServletRequest
import org.jctools.queues.MpscLinkedQueue
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration


/**
 * 跟踪信息服务
 *
 * @author shy
 * @since 2026年04月21日
 */
@Service
class TrackService(
    private val cafeTrackLogMapper: CafeTrackLogMapper,
    private val productService: ProductService,
    private val asyncManager: AsyncManager,
) {
    companion object {
        private val trackLogQueue = MpscLinkedQueue<CafeTrackLog>()
        private val flushLock = ReentrantLock()

        // Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:149.0) Gecko/20100101 Firefox/149.0
        private fun getDeviceType(userAgent: String?): String {
            return userAgent?.substringBefore(";")?.substringBefore(")")?.trim() ?: ""
        }

        private fun getBrowser(userAgent: String?): String {
            return userAgent?.substringAfter(")")?.trim() ?: ""
        }

        private fun getOs(userAgent: String?): String {
            return userAgent?.substringBefore("(")?.trim() ?: ""
        }
    }

    init {
        asyncManager.scheduleAtFixedRate(0L, 5.seconds.toJavaDuration()) {
            processTrackLogs()
        }
    }


    fun track(code: String, eventName: String, request: HttpServletRequest) {
        val dbInfo = productService.getByCode(code)
        val cafeTrackLog = CafeTrackLog().apply {
            prodId = dbInfo?.prodId?.toString()
            prodCode = code
            prodName = dbInfo?.prodName ?: ""
            this.eventName = eventName
            ip = getIpAddr(request)
            location = getRealAddressByIP(ip)
            userAgent = request.getHeader("User-Agent")
            deviceType = request.getHeader("Device-Type") ?: getDeviceType(userAgent)
            browser = request.getHeader("Browser") ?: getBrowser(userAgent)
            os = request.getHeader("OS") ?: getOs(userAgent)
            pageUrl = request.requestURL.toString()
            referrerUrl = request.getHeader("Referer")
            createdAt = Date()
        }
        trackLogQueue.offer(cafeTrackLog)
    }

    /**
     * 定时任务，每5秒处理一次队列中的日志
     */
    fun processTrackLogs() {
        flushLock.withLock {
            val all = mutableListOf<CafeTrackLog>() // 临时存储所有日志
            while (!trackLogQueue.isEmpty) { // 从队列中取出所有日志
                all.add(trackLogQueue.poll()) // 添加到临时存储中
            }
            if (all.isEmpty()) return
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
