package com.satoriviacafe.cafe.service

import com.alipay.mychain.taas.api.request.CodeLogQueryReq
import com.alipay.mychain.taas.api.request.QueryCodeInfoReq
import com.alipay.mychain.taas.api.response.CommonResp
import com.alipay.mychain.taas.api.response.h5.TpCodeInfoResp
import com.alipay.mychain.taas.sdk.service.TraceStatService
import com.alipay.mychain.taas.sdk.service.TraceUserService
import com.satoriviacafe.common.utils.logger
import org.springframework.stereotype.Component

/**
 * 螞蟻鏈信息查詢管理器
 * 僅負責從鏈上查詢已存在的溯源存證數據（由螞蟻後台配置）
 */
@Component
class AntChainManager(
    private val traceUserService: TraceUserService,
    private val traceStatService: TraceStatService
) {

    companion object {
        private val log = logger
    }

    /**
     * 根據溯源码 (code) 查詢鏈上存證詳情
     * 用於 NFC 觸碰後，前端展示咖啡的“區塊鏈存證信息”
     * * @param code NFC 標籤對應的溯源码
     * @return TpCodeInfoResp 包含鏈上所有環節、存證內容和 TxHash
     */
    fun queryTraceDetail(code: String?): TpCodeInfoResp? {
        log.info(">> [螞蟻鏈查詢] 溯源码: {}", code)
        if (code.isNullOrBlank()) return null

        try {
            // 1. 構建請求對象 (適配 QueryCodeInfoReq)
            val request = QueryCodeInfoReq()
            request.traceCode = code

            // 如果你的業務需要區分不同用戶查詢，可以設置 request.readerIdentifier
            // request.readerIdentifier = "CONSUMER_H5"

            // 2. 調用核心查詢接口 (適配你提供的 TraceUserService.queryCodeInfo)
            val response: CommonResp<TpCodeInfoResp>? = traceUserService.queryCodeInfo(request)

            // 3. 處理響應
            if (response != null && response.isSuccess) {
                log.info("<< [螞蟻鏈查詢] 成功獲取鏈上數據")
                return response.data // 返回 TpCodeInfoResp，裡面包含了所有的溯源環節 (Phases)
            } else {
                val errorMsg = response?.message ?: "鏈上無數據"
                log.warn("<< [螞蟻鏈查詢] 失敗: {}", errorMsg)
            }
        } catch (e: Exception) {
            log.error("<< [螞蟻鏈查詢] 調用 SDK 發生異常", e)
        }
        return null
    }

    /**
     * 查詢掃碼次數（利用 TraceStatService）
     * 用於在官網顯示“這是該咖啡第 X 次被驗證真偽”
     */
    fun queryScanCount(code: String?): Int {
        if (code.isNullOrBlank()) return 0
        try {
            val req = CodeLogQueryReq()
            // 注意：根據你的 CodeLogQueryReq 源碼，可能還需要設置 merchantId
            // req.merchantId = "YOUR_MERCHANT_ID"

            val resp = traceStatService.codeScanLog(req)
            if (resp != null && resp.isSuccess) {
                // 如果 data 列表裡過濾出 code 一致的記錄，即為掃碼次數
                return resp.data?.filter { it.code == code }?.size ?: 0
            }
        } catch (e: Exception) {
            log.error("查詢掃碼日誌失敗", e)
        }
        return 0
    }

}
