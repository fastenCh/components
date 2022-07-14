package com.ch.core.ktx

import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.TimeUtils
import com.ch.core.config.TimeConfig
import java.lang.Exception
import java.util.*

fun String?.isNotNullOrEmpty(): Boolean = this.isNullOrEmpty()

fun String.toDate(): Date = TimeUtils.string2Date(this, this.getDateFormat())

fun String.toMillis(): Long = TimeUtils.string2Millis(this, this.getDateFormat())

fun String.getDateFormat(): String {
    when {
        RegexUtils.isMatch("[0-9]{4}", this) -> return TimeConfig.Y
        RegexUtils.isMatch("[0-9]{4}-[0-9]{2}", this) -> return TimeConfig.YM
        RegexUtils.isMatch("[0-9]{4}-[0-9]{2}-[0-9]{2}", this) -> return TimeConfig.YMD
        RegexUtils.isMatch("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}", this) -> return TimeConfig.YMDH
        RegexUtils.isMatch("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}", this) -> return TimeConfig.YMDHM
        RegexUtils.isMatch("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}", this) -> return TimeConfig.YMDHMS
        RegexUtils.isMatch("[0-9]{4}/[0-9]{2}", this) -> return TimeConfig.YM1
        RegexUtils.isMatch("[0-9]{4}/[0-9]{2}/[0-9]{2}", this) -> return TimeConfig.YMD1
        RegexUtils.isMatch("[0-9]{4}/-[0-9]{2}/[0-9]{2} [0-9]{2}", this) -> return TimeConfig.YMDH1
        RegexUtils.isMatch("[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}", this) -> return TimeConfig.YMDHM1
        RegexUtils.isMatch("[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}", this) -> return TimeConfig.YMDHMS1
        else -> throw Exception("unKnow parse date format")
    }
}

fun String.call() {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$this")
    ActivityUtils.startActivity(intent)
}