package com.ch.ui.form

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.ch.ui.R
import com.ch.ui.utils.*

/**
 * 表达输入Label组件，主要用对于存在类型标题+内容+文本三段式的情况
 */
class FormLabel @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defStyleAttr) {
    var mTitle: String? = null
        set(value) {
            field = value
            refreshTitle()
        }
    var mLeftTitle: String? = null
        set(value) {
            field = value
            refreshTitle()
        }
    var mRightTitle: String? = null
        set(value) {
            field = value
            refreshTitle()
        }

    var mLeftTitleTextSize: Int = -1
        set(value) {
            field = value
            refreshTitle()
        }
    var mTitleTextSize: Int = -1
        set(value) {
            field = value
            refreshTitle()
        }
    var mRightTitleTextSize: Int = -1
        set(value) {
            field = value
            refreshTitle()
        }

    var mLeftTitleTextColor: Int = -1
        set(value) {
            field = value
            refreshTitle()
        }
    var mTitleTextColor: Int = -1
        set(value) {
            field = value
            refreshTitle()
        }
    var mRightTitleTextColor: Int = -1
        set(value) {
            field = value
            refreshTitle()
        }

    private var mAndroidTextColor: Int = Color.parseColor("#666666")
    private var mAndroidTextSize: Int = -1
    var mTextMarginLeft: Int = -1
        set(value) {
            field = value
            refreshTitle()
        }
    var mTextMarginRight: Int = -1
        set(value) {
            field = value
            refreshTitle()
        }

    var isInit = false
    private var mLeftDrawable: Drawable? = null

    init {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.FormLabel)
        mTitle = text.toString()
        //=========FormLabel 标题左侧=========
        a.getViewText(R.styleable.FormLabel_leftTitle) { mLeftTitle = it }
        a.getViewSp(R.styleable.FormLabel_leftTitleSize) { mLeftTitleTextSize = it }
        a.getViewColor(R.styleable.FormLabel_leftTitleColor) { mLeftTitleTextColor = it }
        //=========FormLabel 标题部分=========
        a.getViewText(R.styleable.FormLabel_android_title) { mTitle = it }
        a.getViewSp(R.styleable.FormLabel_titleSize) { mTitleTextSize = it }
        a.getViewColor(R.styleable.FormLabel_android_titleTextColor) { mTitleTextColor = it }
        a.getViewDp(R.styleable.FormLabel_titleMarginLeft) { mTextMarginLeft = it }
        a.getViewDp(R.styleable.FormLabel_titleMarginRight) { mTextMarginRight = it }
        //=========FormLabel 标题右侧=========
        a.getViewText(R.styleable.FormLabel_rightTitle) { mRightTitle = it }
        a.getViewSp(R.styleable.FormLabel_rightTitleSize) { mRightTitleTextSize = it }
        a.getViewColor(R.styleable.FormLabel_rightTitleColor) { mRightTitleTextColor = it }
        //=========FormLabel 通用属性=========
        a.getViewSp(R.styleable.FormLabel_android_textSize) { mAndroidTextSize = it }
        a.getViewColor(R.styleable.FormLabel_android_textColor) { mAndroidTextColor = it }
        a.getViewDrawable(R.styleable.FormText_leftDrawable) {
            setPadding(paddingLeft + it.intrinsicWidth, paddingTop, paddingRight, paddingBottom)
            mLeftDrawable = it
        }
        a.recycle()
        isInit = true
        refreshTitle()
    }

    private fun refreshTitle() {
        if (!isInit) return
        text = getWholeText()
    }

    private fun getWholeText(): SpannableStringBuilder {
        val span = SpanUtils()
        //左侧标题部分
        span.append(mLeftTitle ?: "")
        if (mLeftTitleTextColor != -1) {
            span.setForegroundColor(mLeftTitleTextColor)
        } else {
            if (mAndroidTextColor != -1) span.setForegroundColor(mAndroidTextColor)
            if (mTitleTextColor != -1) span.setForegroundColor(mTitleTextColor)
        }
        if (mLeftTitleTextSize != -1) {
            span.setFontSize(mLeftTitleTextSize)
        } else {
            if (mAndroidTextSize != -1) span.setFontSize(mAndroidTextSize)
        }
        if (mTextMarginLeft != -1) span.appendSpace(mTextMarginLeft)

        //内容部分
        span.append(mTitle ?: "")
        if (mTitleTextColor != -1) {
            span.setForegroundColor(mTitleTextColor)
        } else {
            if (mAndroidTextColor != -1) span.setForegroundColor(mAndroidTextColor)
        }
        if (mTitleTextSize != -1) {
            span.setFontSize(mTitleTextSize)
        } else {
            if (mAndroidTextSize != -1) span.setFontSize(mAndroidTextSize)
        }
        if (mTextMarginRight != -1) span.appendSpace(mTextMarginRight)

        //单位部分
        span.append(mRightTitle ?: "")
        if (mRightTitleTextColor != -1) {
            span.setForegroundColor(mRightTitleTextColor)
        } else {
            if (mAndroidTextColor != -1) span.setForegroundColor(mAndroidTextColor)
            if (mTitleTextColor != -1) span.setForegroundColor(mTitleTextColor)
        }
        if (mRightTitleTextSize != -1) {
            span.setFontSize(mLeftTitleTextSize)
        } else {
            if (mAndroidTextSize != -1) span.setFontSize(mAndroidTextSize)
        }
        return span.create()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mLeftDrawable != null) {
            val fl = paddingLeft.toFloat() - mLeftDrawable!!.intrinsicWidth
            canvas.drawBitmap(drawableToBitmap(mLeftDrawable!!), fl, paddingTop.toFloat(), null)
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (isInit) super.setText(getWholeText(), type) else super.setText(text, type)
    }

    override fun getText(): CharSequence {
        var text = super.getText().toString()
        if (!mLeftTitle.isNullOrEmpty() && text.startsWith(mLeftTitle!!)) {
            var length = mLeftTitle!!.length
            if (mTextMarginLeft != -1) length += 3
            text = text.substring(length)
        }
        if (!mRightTitle.isNullOrEmpty() && text.endsWith(mRightTitle!!)) {
            var length = text.length - mRightTitle!!.length
            if (mTextMarginRight != -1) length -= 3
            text = text.substring(0, length)
        }
        return text
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        } else {
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

}