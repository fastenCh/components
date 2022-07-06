package com.ch.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes

/**
 *  空白的状态栏
 * @author ch
 * @date 2022年7月6日14:04:47
 */
class StatusBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        setPaddingRelative(
            paddingStart,
            paddingTop + getStatusBarHeight(),
            paddingEnd,
            paddingBottom
        )
    }

    /**
     * 获取顶部栏的高度
     */
    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
}