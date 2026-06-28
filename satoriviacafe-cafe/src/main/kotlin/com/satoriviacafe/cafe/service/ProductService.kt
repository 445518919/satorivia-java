package com.satoriviacafe.cafe.service

import com.satoriviacafe.cafe.domain.CafeProduct
import com.satoriviacafe.cafe.mapper.CafeProductMapper
import com.satoriviacafe.cafe.service.impl.CafeProductServiceImpl
import com.satoriviacafe.common.core.redis.RedisCache
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service


@Service
@Primary
class ProductService(
    private val cafeProductMapper: CafeProductMapper,
    private val redisCache: RedisCache,
) : CafeProductServiceImpl(cafeProductMapper) {

    companion object {
        const val CACHE_PREFIX = "cafe:product:"
        fun getCacheKey(productId: Long): String {
            return "$CACHE_PREFIX$productId"
        }

        val nullGuard = CafeProduct()
    }

    fun getById(productId: Long): CafeProduct? {
        val cacheKey = getCacheKey(productId)
        val cached = redisCache.getCacheObject<CafeProduct>(cacheKey)

        // 如果缓存中有数据且 prodId 不为空，说明是真的缓存对象
        if (cached != null && cached.productId != null) return cached
        // 如果缓存对象不为空但 prodId 为空，说明是之前存入的 nullGuard
        if (cached != null && cached.productId == null) return null

        val result = cafeProductMapper.selectCafeProductByProductId(productId)
        // 存入时：result 为 null 就存入一个空的 CafeProduction 对象（标记 prodId 为空）
        redisCache.setCacheObject(cacheKey, result ?: nullGuard)
        return result
    }
}
