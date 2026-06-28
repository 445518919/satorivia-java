package com.satoriviacafe.cafe.service

import com.satoriviacafe.cafe.domain.CafeProductNfc
import com.satoriviacafe.cafe.service.impl.CafeProductNfcServiceImpl
import com.satoriviacafe.cafe_web.mapper.ProductNfcMapper
import com.satoriviacafe.common.exception.ServiceException
import com.satoriviacafe.common.utils.VStringUtils
import com.satoriviacafe.common.utils.logger
import org.apache.commons.lang3.RandomStringUtils.secure
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
class ProductNfcService(
    private val nfcMapper: ProductNfcMapper,
) : CafeProductNfcServiceImpl(nfcMapper), ICafeProductNfcService {
    private val log = logger

    companion object {
        private const val NFC_URL = "https://nfc.satoriviacafe.com"
    }

    override fun importCafeProductNfc(
        nfcList: List<CafeProductNfc>?,
        updateSupport: Boolean,
        operName: String?
    ): String {
        if (VStringUtils.isNull(nfcList) || nfcList.isNullOrEmpty()) {
            throw ServiceException("导入用户数据不能为空！")
        }
        var successNum = 0
        var failureNum = 0
        val successMsg = StringBuilder()
        val failureMsg = StringBuilder()
        for (nfc in nfcList) {
            try {
                if (nfc.nfcCode == null) throw RuntimeException("nfcCode 不能为空")
                if (nfc.skuCode.isNullOrEmpty()) throw RuntimeException("skuCode 不能为空")
                if (nfc.productName.isNullOrEmpty()) throw RuntimeException("productName 不能为空")

                nfc.nfcHash = secure().nextAlphanumeric(32)
                nfc.nfcUrl = "$NFC_URL/${nfc.nfcCode}?k=${nfc.nfcHash}"
                // 验证是否存在这个
                val n = nfcMapper.selectByNfcCode(nfc.nfcCode)
                if (n == null) {
                    nfcMapper.insertCafeProductNfc(nfc)
                    successNum++
                    successMsg.append("<br/>").append(successNum).append("、NFC ").append(nfc.nfcCode)
                        .append(" 导入成功")
                } else if (updateSupport) {
                    nfcMapper.updateCafeProductNfc(nfc)
                    successNum++
                    successMsg.append("<br/>").append(successNum).append("、NFC ").append(nfc.nfcCode)
                        .append(" 更新成功")
                } else {
                    failureNum++
                    failureMsg.append("<br/>").append(failureNum).append("、NFC ").append(nfc.nfcCode)
                        .append(" 已存在")
                }
            } catch (e: Exception) {
                failureNum++
                val msg: String = "<br/>${failureNum}、NFC ${nfc.nfcCode} 导入失败："
                failureMsg.append(msg).append(e.message)
                log.error(msg, e)
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 $failureNum 条数据格式不正确，错误如下：")
            throw ServiceException(failureMsg.toString())
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 $successNum 条，数据如下：")
        }
        return successMsg.toString()
    }

}
