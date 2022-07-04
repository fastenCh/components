package com.ch.ui.form

import android.content.Context
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

    var isInit = false
    var mTextColor: Int = -1

    var mTitle: String? = null
        set(value) {
            field = value
            applyText()
        }
    var mUnit: String? = null
        set(value) {
            field = value
            applyText()
        }

    var mTextSize: Int = -1
        set(value) {
            field = value
            applyText()
        }

    var mTitleTextSize: Int = -1
        set(value) {
            field = value
            applyText()
        }
    var mUnitTextSize: Int = -1
        set(value) {
            field = value
            applyText()
        }

    var mAndroidTextColor: Int = -1
        set(value) {
            field = value
            applyText()
        }
    var mTitleTextColor: Int = -1
        set(value) {
            field = value
            applyText()
        }
    var mUnitTextColor: Int = -1
        set(value) {
            field = value
            applyText()
        }

    var mTextMarginLeft: Int = -1
        set(value) {
            field = value
            applyText()
        }
    var mTextMarginRight: Int = -1
        set(value) {
            field = value
            applyText()
        }


    init {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.FormLabel)
        a.getViewText(R.styleable.FormLabel_android_title) { mTitle = it }
        a.getViewText(R.styleable.FormLabel_unit) { mUnit = it }

        a.getViewSp(R.styleable.FormLabel_titleTextSize) { mTitleTextSize = it }
        a.getViewSp(R.styleable.FormLabel_unitTextSize) { mUnitTextSize = it }
        a.getViewSp(R.styleable.FormLabel_android_textSize) { mTextSize = it }

        a.getViewColor(R.styleable.FormLabel_android_titleTextColor) { mTitleTextColor = it }
        a.getViewColor(R.styleable.FormLabel_unitTextColor) { mUnitTextColor = it }
        a.getViewColor(R.styleable.FormLabel_android_textColor) { mAndroidTextColor = it }

        a.getViewDp(R.styleable.FormLabel_textMarginLeft) { mTextMarginLeft = it }
        a.getViewDp(R.styleable.FormLabel_textMarginRight) { mTextMarginRight = it }

        a.getViewColor(R.styleable.FormLabel_textColor) { mTextColor = it }

        a.recycle()
        isInit = true
        applyText()
    }

    fun applyText(text: String?) {
        this.text = text ?: ""
        applyText()
    }

    private fun applyText() {
        if (!isInit) return
        val span = SpanUtils.with(this)
        //标题部分
        span.append(mTitle ?: "")
        if (mTitleTextColor != -1) span.setForegroundColor(mTitleTextColor) else {
            if (mTextColor != -1) span.setForegroundColor(mTextColor)
        }
        if (mTitleTextSize != -1) span.setFontSize(mTitleTextSize) else {
            if (mTextSize != -1) span.setFontSize(mTextSize)
        }
        if (mTextMarginLeft != -1) span.appendSpace(mTextMarginLeft)
        //内容部分
        span.append(text ?: "")
        if (mAndroidTextColor != -1) span.setForegroundColor(mAndroidTextColor) else {
            if (mTextColor != -1) span.setForegroundColor(mTextColor)
        }
        if (mTextSize != -1) span.setFontSize(mTextSize)
        if (mTextMarginRight != -1) span.appendSpace(mTextMarginRight)
        //单位部分
        span.append(mUnit ?: "")
        if (mUnitTextColor != -1) span.setForegroundColor(mUnitTextColor) else {
            if (mTextColor != -1) span.setForegroundColor(mTextColor)
        }
        if (mUnitTextSize != -1) span.setFontSize(mTitleTextSize) else {
            if (mTextSize != -1) span.setFontSize(mTextSize)
        }
        span.create()
    }

    override fun getText(): CharSequence {
        var text = super.getText().toString()
        if (!mTitle.isNullOrEmpty() && text.startsWith(mTitle!!)) {
            var length = mTitle!!.length
            if (mTextMarginLeft != -1) length += 3
            text = text.substring(length)
        }
        if (!mUnit.isNullOrEmpty() && text.endsWith(mUnit!!)) {
            var length = text.length - mUnit!!.length
            if (mTextMarginRight != -1) length -= 3
            text = text.substring(0, length)
        }
        return text
    }
}