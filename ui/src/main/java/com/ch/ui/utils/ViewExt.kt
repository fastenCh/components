package com.ch.ui.utils

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

object ViewExt {
    fun TextView.setLeftDrawable(drawable: Drawable?) {
        this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }

    fun TextView.setLeftDrawableTint(color: Int) {
        var drawable: Drawable = this.compoundDrawables[2]
        drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(drawable.mutate(), ContextCompat.getColor(this.context, color))
        setLeftDrawable(drawable)
    }

    fun TextView.setLeftDrawableSize(size: Int) {
        val drawable: Drawable = this.compoundDrawables[0]
        if (size > 0) {
            drawable.setBounds(0, 0, size, size)
        } else {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        }
        setLeftDrawable(drawable)
    }

    fun TextView.setRightDrawable(drawable: Drawable?) {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }

    fun TextView.setRightDrawableTint(color: Int) {
        var drawable: Drawable = this.compoundDrawables[2]
        drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(drawable.mutate(), ContextCompat.getColor(this.context, color))
        setRightDrawable(drawable)
    }

    fun TextView.setRightDrawableSize(size: Int) {
        val drawable: Drawable = this.compoundDrawables[2]
        if (size > 0) {
            drawable.setBounds(0, 0, size, size)
        } else {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        }
        setRightDrawable(drawable)
    }
}