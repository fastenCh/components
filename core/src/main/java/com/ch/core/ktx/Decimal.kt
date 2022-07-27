package com.ch.core.ktx

import com.ch.core.config.Constants
import java.math.BigDecimal

fun Any?.toDecimal(): BigDecimal {
    if (this == null || this.toString().trim() == Constants.STR_EMPTY || this.toString().trim() == Constants.STR_NULL || this.toString().trim() == Constants.STR_PLACE) {
        return BigDecimal(Constants.STR_ZERO)
    }
    return try {
        BigDecimal(this.toString())
    } catch (e: Exception) {
        BigDecimal(Constants.STR_ZERO)
    }
}

fun BigDecimal.add(value: Any?): BigDecimal = this.add(value.toDecimal())

fun BigDecimal.sub(value: Any?): BigDecimal = this.subtract(value.toDecimal())

fun BigDecimal.mul(value: Any?): BigDecimal = this.multiply(value.toDecimal())

fun BigDecimal.div(value: Any?, roundingMode: Int = 2): BigDecimal = this.divide(value.toDecimal(), roundingMode)

fun BigDecimal.parse(newScale: Int = 2, roundingMode: Int = BigDecimal.ROUND_HALF_UP): BigDecimal = this.setScale(newScale, roundingMode)

fun BigDecimal.toText(): String = this.toPlainString()