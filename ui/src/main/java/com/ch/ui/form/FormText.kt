package com.ch.ui.form

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.ch.ui.R
import com.ch.ui.utils.getViewColor
import com.ch.ui.utils.getViewSp
import com.ch.ui.utils.getViewText

class FormText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FormLayout(context, attributeSet, defStyleAttr) {
    val mTvText: TextView
    private var mIsCanEdit = true    //设置点击事件开关，用于同一个界面下点击事件的开关

    init {
        inflate(context, R.layout.layout_form_text, this)
        mTvText = findViewById(R.id.tv_text)
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.FormText)
        //text
        a.getViewText(R.styleable.FormText_android_text, this::setText)
        a.getViewText(R.styleable.FormText_android_hint, this::setHintText)
        a.getViewColor(R.styleable.FormText_android_textColor, this::setTextColor)
        a.getViewSp(R.styleable.FormText_android_textSize, this::setTextSize)
        a.getViewSp(R.styleable.FormText_android_gravity, this::setTextGravity)
        a.recycle()
    }

    /** 获取内容 */
    fun getText() = mTvText.text.toString().trim()

    /** 设置是否可编辑 */
    fun setIsCanEdit(isCanEdit: Boolean) = kotlin.run { mIsCanEdit = isCanEdit }

    /** 设置允许编辑 */
    fun getIsCanEdit() = mIsCanEdit

    /** Text属性设置 */
    fun setText(title: CharSequence? = null) = setText(title.toString())
    fun setText(title: String? = null) = run { mTvText.text = title ?: "" }
    fun setHintText(title: String? = null) = run { mTvText.hint = title ?: "" }
    fun setTextColor(titleTextColor: Int) = mTvText.setTextColor(titleTextColor)
    fun setTextSize(titleTextSize: Int) = run { mTvText.textSize = titleTextSize.toFloat() }
    fun setTextGravity(gravity: Int) = run { mTvText.gravity = gravity }

    fun setTextClickCenter(block: () -> Unit) {
        mTvText.setOnClickListener { if (mIsCanEdit) block() }
        //未设置点击事件时，监听单位的点击事件，用于扩大点击事件范围
        if (!isLisUnitClick) mTvUnit.setOnClickListener { if (mIsCanEdit) block() }
    }
}