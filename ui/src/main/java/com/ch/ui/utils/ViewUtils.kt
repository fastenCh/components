package com.ch.ui.utils

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.StyleableRes

fun TypedArray.getViewAttribute(@StyleableRes index: Int, success: (res: Int) -> Unit, error: () -> Unit) {
    if (hasValue(index)) {
        val resourceId = getResourceId(index, -1)
        if (resourceId != -1) {
            success(resourceId)
        }
    }
}

fun TypedArray.getViewDrawable(@StyleableRes index: Int, success: (drawable: Drawable) -> Unit) {
    if (hasValue(index)) {
        val drawable = getDrawable(index)
        if (drawable != null) {
            success(drawable)
        }
    }
}

fun TypedArray.getViewText(@StyleableRes index: Int, success: (charSequence: String) -> Unit) {
    if (hasValue(index)) {
        val resourceId = getResourceId(index, -1)
        if (resourceId == -1) {
            success(this.getString(index) ?: "")
        } else {
            success(resources.getString(resourceId))
        }
    }
}

fun TypedArray.getViewDp(@StyleableRes index: Int, success: (size: Int) -> Unit) {
    if (hasValue(index)) {
        val size = getDimensionPixelSize(index, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1F, Resources.getSystem().displayMetrics).toInt())
        success(size)
    }
}

fun TypedArray.getViewSp(@StyleableRes index: Int, success: (size: Int) -> Unit) {
    if (hasValue(index)) {
        val size = getDimensionPixelSize(index, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, -1F, Resources.getSystem().displayMetrics).toInt())
        success(size)
    }
}

fun TypedArray.getViewColor(@StyleableRes index: Int, success: (color: Int) -> Unit) {
    if (hasValue(index)) {
        val color = getColor(index, -1)
        success(color)
    }
}

fun TypedArray.getViewBool(@StyleableRes index: Int, success: (isTure: Boolean) -> Unit) {
    if (hasValue(index)) {
        val value = getBoolean(index, false)
        success(value)
    }
}

fun TypedArray.getViewInteger(@StyleableRes index: Int, success: (value: Int) -> Unit) {
    if (hasValue(index)) {
        val value = getInteger(index, 0)
        success(value)
    }
}