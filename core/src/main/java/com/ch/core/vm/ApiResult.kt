package com.ch.core.vm

import androidx.annotation.Keep

@Keep
open class ApiResult<T>(
    open var data: T?,
) : Api()