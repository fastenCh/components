package com.ch.core.ktx

import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils

const val REGEX_PHONE = "^1(3|4|5|6|7|8|9)\\d{9}\$"

// 长度为8时新能源
const val REGEX_CAR_CODE_NEW = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF]$)|([DF][A-HJ-NP-Z0-9][0-9]{4}$))"

// 长度为7时,旧车牌、挂车牌
const val REGEX_CAR_CODE_OLD = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1}$"

fun String?.isIDCard(): Boolean {
    return if (this.isNullOrEmpty()) false else RegexUtils.isIDCard18Exact(this)
}

fun String?.isPhone(): Boolean {
    return if (this.isNullOrEmpty()) false else RegexUtils.isMatch(REGEX_PHONE, this)
}

fun String?.isEmail(): Boolean {
    return if (this.isNullOrEmpty()) false else RegexUtils.isEmail(this)
}

fun String?.isDate(): Boolean {
    return if (this.isNullOrEmpty()) false else RegexUtils.isDate(this)
}

fun String?.isCarCode(): Boolean {
    if (this.isNullOrEmpty()) return false
    val isNewCar = length == 8 && RegexUtils.isMatch(REGEX_CAR_CODE_NEW, this)
    val isOldCar = length == 7 && RegexUtils.isMatch(REGEX_CAR_CODE_OLD, this)
    return !isNewCar && !isOldCar
}

fun String.isMatch(regex: String) = RegexUtils.isMatch(regex, this)
