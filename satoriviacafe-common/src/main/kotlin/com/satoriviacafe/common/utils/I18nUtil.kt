package com.satoriviacafe.common.utils

infix fun String.i18n(arg: Any?): String = MessageUtils.message(this, arg)
infix fun String.i18n(args: Array<out Any?>): String = MessageUtils.message(this, *args)
inline val String.i18n: String get() = MessageUtils.message(this)

