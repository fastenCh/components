package com.ch.core.ktx

import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils

fun String?.show() = ToastUtils.showShort(this ?: "")

fun Int.show() = ToastUtils.showShort(this)

fun Boolean.show(msg: String): Boolean {
    if (!this) msg.show()
    return this
}

fun TextView.isShowHint(): Boolean {
    val isNull = this.text.toString().trim().isEmpty()
    if (isNull) (hint ?: "${text}错误").toString().show()
    return isNull
}

fun EditText.isShowHint(): Boolean {
    val isNull = this.text.toString().trim().isEmpty()
    if (isNull) (hint ?: "${text}错误").toString().show()
    return isNull
}

fun Int.isZeroShow(msg: String): Boolean {
    val isZero = this == 0
    if (isZero) msg.show()
    return isZero
}