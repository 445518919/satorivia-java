package com.satoriviacafe.cafe.service

import com.satoriviacafe.cafe.domain.CafeProduction
import com.satoriviacafe.cafe.domain.ProductDetail
import org.springframework.stereotype.Service


@Service
class ProductService(
    private val cafeProductionService: ICafeProductionService,
    private val antChainManager: AntChainManager,
) {
    fun detail(code: String): ProductDetail {
        val chainInfo = antChainManager.queryTraceDetail(code) ?: throw RuntimeException("No chain found") // 螞蟻鏈上找不到溯源碼
        val productCode =
            chainInfo.productInfo.productCode ?: throw RuntimeException("No product code found") // 螞蟻鏈上找不到商品編碼
        val dbInfo = cafeProductionService.selectCafeProductionList(
            CafeProduction().apply { prodCode = productCode }
        ).firstOrNull() ?: throw RuntimeException("No product found") // 數據庫找不到對應的商品
        return ProductDetail(
            dbInfo,    // 官網配置的漂亮圖文
            chainInfo // 螞蟻鏈上的硬核存證
        )
    }
}
