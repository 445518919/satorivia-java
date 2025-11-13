package com.satoriviacafe.common.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable

/**
 * 一个让每个协程都跑在虚拟线程上的 CoroutineDispatcher。
 * 你可以直接在 withContext/launch/async 等协程构建器中使用它。
 */
object VirtualThreadDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: kotlin.coroutines.CoroutineContext, block: Runnable) {
        Thread.ofVirtual()
            .name("kotlin-virtual-thread")
            .start { block.run() }
    }
}
