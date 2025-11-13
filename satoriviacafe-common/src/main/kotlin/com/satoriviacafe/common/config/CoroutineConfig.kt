package com.satoriviacafe.common.config

import com.satoriviacafe.common.utils.VirtualThreadDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author shy
 * @since 2025年06月25日
 */
@Configuration
class CoroutineConfig {
    @Bean
    fun virtualThreadDispatcher(): CoroutineDispatcher = VirtualThreadDispatcher
}
