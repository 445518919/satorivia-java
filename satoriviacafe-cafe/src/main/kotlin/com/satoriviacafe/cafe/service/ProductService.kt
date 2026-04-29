package com.satoriviacafe.cafe.service

import com.satoriviacafe.cafe.domain.CafeProduction
import com.satoriviacafe.cafe.mapper.CafeProductionMapper
import com.satoriviacafe.cafe.service.impl.CafeProductionServiceImpl
import com.satoriviacafe.common.core.redis.RedisCache
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service


@Service
@Primary
class ProductService(
    private val cafeProductionMapper: CafeProductionMapper,
    private val redisCache: RedisCache,
) : CafeProductionServiceImpl(cafeProductionMapper) {

    companion object {
        const val CACHE_PREFIX = "cafe:product:"
        fun getCacheKey(code: String): String {
            return "$CACHE_PREFIX$code"
        }

        val nullGuard = CafeProduction()
    }

    fun getByCode(code: String): CafeProduction? {
        val cacheKey = getCacheKey(code)
        val cached = redisCache.getCacheObject<CafeProduction>(cacheKey)

        // 如果缓存中有数据且 prodId 不为空，说明是真的缓存对象
        if (cached != null && cached.prodId != null) return cached
        // 如果缓存对象不为空但 prodId 为空，说明是之前存入的 nullGuard
        if (cached != null && cached.prodId == null) return null

        val result =
            cafeProductionMapper.selectCafeProductionList(CafeProduction().apply { prodCode = code }).firstOrNull()

        // 存入时：result 为 null 就存入一个空的 CafeProduction 对象（标记 prodId 为空）
        redisCache.setCacheObject(cacheKey, result ?: nullGuard)
        return result
    }
}
