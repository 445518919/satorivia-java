package com.satoriviacafe.common.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

// 创建一个日志扩展属性
inline val <T : Any> T.logger: Logger
    get() = LoggerFactory.getLogger(this::class.java)
