package com.ch.ui.form

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.InspectableProperty
import com.ch.ui.R
import com.ch.ui.utils.*
import org.w3c.dom.Text
import kotlin.math.max

class FormFiled @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FormLayout(context, attributeSet, defStyleAttr) {

    val mEtText: EditText
    var isCanClick = true    //设置点击事件开关，用于同一个界面下点击事件的开关
    private var maxLength: Int = -1
    private var decimalLength: Int = -1
    private var integerLength: Int = -1
    private var mlengthFilter: InputFilter.LengthFilter? = null
    private var mDecimalDigitsInputFilter: DecimalDigitsInputFilter? = null

    init {
        inflate(context, R.layout.layout_form_filed, this)
        mEtText = findViewById(R.id.et_text)
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.FormFiled)
        //text
        a.getViewText(R.styleable.FormFiled_android_text, this::setText)
        a.getViewText(R.styleable.FormFiled_android_hint, this::setHintText)
        a.getViewColor(R.styleable.FormFiled_android_textColor, this::setTextColor)
        a.getViewSp(R.styleable.FormFiled_android_textSize, this::setTextSize)
        a.getViewSp(R.styleable.FormFiled_android_gravity, this::setTextGravity)
        a.getViewInteger(R.styleable.FormFiled_android_maxLength) { maxLength = it }
        a.getViewInteger(R.styleable.FormFiled_decimalLength) { decimalLength = it }
        a.getViewInteger(R.styleable.FormFiled_integerLength) { integerLength = it }
        a.getViewInteger(R.styleable.FormFiled_android_inputType) { mEtText.inputType = it }
        a.getViewText(R.styleable.FormFiled_android_digits) { mEtText.keyListener = DigitsKeyListener.getInstance(it) }
        updateFilter()
        a.recycle()
    }

    fun setDigitsLength(integerLength: Int? = null, maxLength: Int? = null) {
        if (integerLength != null) this.integerLength = integerLength
        if (maxLength != null) this.maxLength = maxLength
        updateFilter()
    }

    private fun updateFilter() {
        if (maxLength < 0 && decimalLength < 0) {
            return
        }
        val filter = mEtText.filters.toMutableList()
        if (maxLength > 0) {
            if (mlengthFilter != null) filter.remove(mlengthFilter)
            mlengthFilter = InputFilter.LengthFilter(maxLength)
            filter.add(mlengthFilter!!)
        }
        if (decimalLength > 0) {
            if (mDecimalDigitsInputFilter != null) filter.remove(mDecimalDigitsInputFilter)
            mDecimalDigitsInputFilter = if (integerLength > 1) {
                DecimalDigitsInputFilter().setDigits(integerLength, decimalLength)
            } else {
                DecimalDigitsInputFilter().setDigits(decimalLength)
            }
            filter.add(mDecimalDigitsInputFilter!!)
        }
        mEtText.filters = filter.toTypedArray()
    }

    /** Text属性设置 */
    fun setText(title: CharSequence? = null) = setText(title.toString())
    fun setText(title: String? = null) = run { mEtText.setText(title ?: "") }
    fun setHintText(title: String? = null) = run { mEtText.hint = title ?: "" }
    fun setTextColor(titleTextColor: Int) = mEtText.setTextColor(titleTextColor)
    fun setTextSize(titleTextSize: Int) = run { mEtText.textSize = titleTextSize.toFloat() }
    fun setTextGravity(gravity: Int) = run { mEtText.gravity = gravity }

    fun setTextClickCenter(block: () -> Unit) {
        mEtText.setOnClickListener { if (isCanClick) block() }
        //未设置点击事件时，监听单位的点击事件，用于扩大点击事件范围
        if (!isLisUnitClick) mTvUnit.setOnClickListener { if (isCanClick) block() }
    }
}