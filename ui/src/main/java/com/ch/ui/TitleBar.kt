package com.ch.ui

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.ch.ui.utils.*

/**
 * @author ch
 * @daye 2022年7月7日14:30:19
 */
class TitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var mIvLeft: ImageView
    var mTvTitle: TextView
    var mTvRight1: TextView
    var mTvRight2: TextView
    var mIvRight1: ImageView
    var mIvRight2: ImageView
    var mView: View

    companion object {
        var mBgColor: Int = -1
        var mTextColor: Int = Color.parseColor("#FFFFFF")
    }

    init {
        if (mBgColor == -1) {
            val array: TypedArray = context.theme.obtainStyledAttributes(intArrayOf(androidx.appcompat.R.attr.colorPrimary))
            mBgColor = array.getColor(0, Color.TRANSPARENT)
        }
        if (background == null) setBackgroundColor(mBgColor!!)
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this, true)
        mIvLeft = findViewById(R.id.iv_left)
        mTvTitle = findViewById(R.id.tv_title)
        mTvRight1 = findViewById(R.id.tv_right1)
        mTvRight2 = findViewById(R.id.tv_right2)
        mIvRight1 = findViewById(R.id.iv_right1)
        mIvRight2 = findViewById(R.id.iv_right2)
        mView = findViewById(R.id.view)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar)
        a.getViewDrawable(R.styleable.TitleBar_leftDrawable) { mIvLeft.setImageDrawable(it)  }

        a.getViewText(R.styleable.TitleBar_android_title) {
            mTvTitle.text = it
            mTvTitle.visibility = VISIBLE
        }
        if (a.hasValue(R.styleable.TitleBar_android_textColor)) {
            a.getViewColor(R.styleable.TitleBar_android_textColor) { mTvTitle.setTextColor(it) }
        } else {
            mTvTitle.setTextColor(mTextColor)
        }
        a.getViewSp(R.styleable.TitleBar_android_textSize) { mTvTitle.setTextSize(it.toFloat()) }

        a.getViewText(R.styleable.TitleBar_rightTitle1) {
            mTvRight1.text = it
            mTvRight1.visibility = VISIBLE
        }
        if (a.hasValue(R.styleable.TitleBar_rightTitleColor1)) {
            a.getViewColor(R.styleable.TitleBar_rightTitleColor1) { mTvRight1.setTextColor(it) }
        } else {
            mTvRight1.setTextColor(mTextColor)
        }

        a.getViewSp(R.styleable.TitleBar_rightTitleSize2) { mTvRight2.textSize = it.toFloat() }
        a.getViewText(R.styleable.TitleBar_rightTitle2) {
            mTvRight2.text = it
            mTvRight2.visibility = VISIBLE
        }
        if (a.hasValue(R.styleable.TitleBar_rightTitleColor2)) {
            a.getViewColor(R.styleable.TitleBar_rightTitleColor2) { mTvRight2.setTextColor(it) }
        } else {
            mTvRight2.setTextColor(mTextColor)
        }
        a.getViewSp(R.styleable.TitleBar_rightTitleSize1) { mTvRight2.setTextSize(it.toFloat()) }

        a.getViewDrawable(R.styleable.TitleBar_rightDrawable1) {
            mIvRight1.background = it
            mIvRight1.visibility = VISIBLE
        }
        a.getViewDrawable(R.styleable.TitleBar_rightDrawable2) {
            mIvRight2.background = it
            mIvRight2.visibility = VISIBLE
        }

        a.getViewBool(R.styleable.TitleBar_isShowLine) { mView.visibility = if (it) View.VISIBLE else View.GONE }
        a.getViewBool(R.styleable.TitleBar_isShowLine) {
            //是否为沉浸式，主要用于兼容viewpager中单独适配沉浸式的问题
            this.setPaddingRelative(
                this.paddingStart, this.paddingTop + getStatusBarHeight(), this.paddingEnd, this.paddingBottom
            )
        }
        a.getViewBool(R.styleable.TitleBar_isBack) {
            if (it) {
                val drawable = ContextCompat.getDrawable(context, R.drawable.ic_back)!!
                DrawableCompat.wrap(drawable)
                DrawableCompat.setTint(drawable.mutate(), mTextColor)
                drawable.setBounds(0, 0, 24, 24)
                mIvLeft.background = drawable
                if (context is Activity) {
                    setLeftClickListener {
                        context.finish()
                    }
                }
            }
        }
        a.recycle()
    }

    /**
     * 获取顶部栏的高度
     */
    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    fun setLeftClickListener(block: () -> Unit) {
        mIvLeft.setOnClickListener { block() }
    }

    fun setRightText1ClickListener(block: () -> Unit) {
        mTvRight1.setOnClickListener { block() }
    }

    fun setRightText2ClickListener(block: () -> Unit) {
        mTvRight2.setOnClickListener { block() }
    }

    fun setRightDrawable1ClickListener(block: () -> Unit) {
        mIvRight1.setOnClickListener { block() }
    }

    fun setRightDrawable2ClickListener(block: () -> Unit) {
        mIvRight2.setOnClickListener { block() }
    }

}
