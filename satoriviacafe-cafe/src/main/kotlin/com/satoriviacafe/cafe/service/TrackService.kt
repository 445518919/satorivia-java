package com.satoriviacafe.cafe.service

import com.satoriviacafe.cafe.domain.CafeTrackLog
import com.satoriviacafe.cafe.mapper.CafeTrackLogMapper
import com.satoriviacafe.common.utils.ip.AddressUtils.getRealAddressByIP
import com.satoriviacafe.common.utils.ip.IpUtils.getIpAddr
import com.satoriviacafe.framework.manager.AsyncManager
import jakarta.annotation.PreDestroy
import jakarta.servlet.http.HttpServletRequest
import nl.basjes.parse.useragent.UserAgentAnalyzer
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
    private val uaa: UserAgentAnalyzer,
    asyncManager: AsyncManager,
) {
    companion object {
        private val trackLogQueue = MpscLinkedQueue<CafeTrackLog>()
        private val flushLock = ReentrantLock()
    }

    private fun parseUA(uaString: String?): Triple<String?, String?, String?> {
        val agent = uaa.parse(uaString)
        return Triple(
            agent.getValue("OperatingSystemNameVersion"),
            agent.getValue("DeviceClass"),
            agent.getValue("AgentNameVersion")
        )
    }

    init {
        asyncManager.scheduleAtFixedRate(0L, 5.seconds.toJavaDuration()) {
            processTrackLogs()
        }
    }


    fun track(productId: Long, eventName: String, request: HttpServletRequest) {
        val dbInfo = productService.getById(productId)
        val (os, deviceType, browser) = parseUA(request.getHeader("User-Agent"))
        val cafeTrackLog = CafeTrackLog().apply {
            prodId = productId.toString()
            prodName = dbInfo?.productName ?: ""
            this.eventName = eventName
            ip = getIpAddr(request)
            location = getRealAddressByIP(ip)
            userAgent = request.getHeader("User-Agent")
            this.deviceType = request.getHeader("Device-Type") ?: deviceType
            this.browser = request.getHeader("Browser") ?: browser
            this.os = request.getHeader("OS") ?: os
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
