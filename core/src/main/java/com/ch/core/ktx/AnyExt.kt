package com.ch.core.ktx

import com.blankj.utilcode.util.ObjectUtils
import java.text.DecimalFormat

fun Any?.isEmpty() = ObjectUtils.isEmpty(this)

fun Any?.nullToStr(str: String): String = if (this.isEmpty()) str else this.toString()

fun Any?.toWeight() = this.toDecimal().parse(3).toString()

fun Any?.toMoney() = this.toDecimal().parse().toString()

fun Any?.toMajorMoney(): String {
    val money = this.toMoney()
    val decimalFormat = DecimalFormat("#,###.00")
    var newMoney = decimalFormat.format(money.toDouble())
    if (newMoney.startsWith(".")) newMoney = "0$newMoney"
    return newMoney
}