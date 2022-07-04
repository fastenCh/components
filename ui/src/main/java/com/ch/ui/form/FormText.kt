package com.ch.ui.form

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.ch.ui.R
import com.ch.ui.utils.getViewColor
import com.ch.ui.utils.getViewSp
import com.ch.ui.utils.getViewText

class FormText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {
    val mTvTitle: TextView
    val mTvText: TextView
    val mTvUnit: TextView

    val mTitleStart: String? = null
    val mTitleEnd: String? = null

    init {
        inflate(context, R.layout.layout_form_text, this)
        mTvTitle = findViewById(R.id.tv_title)
        mTvText = findViewById(R.id.tv_text)
        mTvUnit = findViewById(R.id.tv_unit)

        val a = context.obtainStyledAttributes(attributeSet, R.styleable.FormText)
        //title
        a.getViewText(R.styleable.FormText_android_title, this::setTitleText)
        a.getViewColor(R.styleable.FormText_android_titleTextColor, this::setTitleTextColor)
        a.getViewSp(R.styleable.FormText_titleTextSize, this::setTitleTextSize)

        //text
        a.getViewText(R.styleable.FormText_android_text, this::setText)
        a.getViewColor(R.styleable.FormText_android_textColor, this::setTextColor)
        a.getViewSp(R.styleable.FormText_android_textSize, this::setTextSize)

        //unit
        a.getViewText(R.styleable.FormText_unit, this::setUnitText)
        a.getViewColor(R.styleable.FormText_unitTextColor, this::setUnitTextColor)
        a.getViewSp(R.styleable.FormText_unitTextSize, this::setUnitTextSize)

        a.recycle()
    }

    /** Title属性设置 */
    fun setTitleText(title: CharSequence? = null) = setTitleText(title.toString())
    fun setTitleText(title: String? = null) = run { mTvTitle.text = title ?: "" }
    fun setTitleTextColor(titleTextColor: Int) = mTvTitle.setTextColor(titleTextColor)
    fun setTitleTextSize(titleTextSize: Int) = run { mTvTitle.textSize = titleTextSize.toFloat() }

    /** Text属性设置 */
    fun setText(title: CharSequence? = null) = setText(title.toString())
    fun setText(title: String? = null) = run { mTvText.text = title ?: "" }
    fun setTextColor(titleTextColor: Int) = mTvText.setTextColor(titleTextColor)
    fun setTextSize(titleTextSize: Int) = run { mTvText.textSize = titleTextSize.toFloat() }

    /** Unit属性设置 */
    fun setUnitText(title: CharSequence? = null) = setUnitText(title.toString())
    fun setUnitText(title: String? = null) = run { mTvUnit.text = title ?: "" }
    fun setUnitTextColor(titleTextColor: Int) = mTvUnit.setTextColor(titleTextColor)
    fun setUnitTextSize(titleTextSize: Int) = run { mTvUnit.textSize = titleTextSize.toFloat() }
}