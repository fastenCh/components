package com.ch.core.vm

import androidx.annotation.Keep

@Keep
open class ApiPage<T>(
    val rows: T,
    val total: Int,
) : Api()