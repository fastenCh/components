package com.ch.ui.form

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.text.method.KeyListener
import android.util.AttributeSet
import android.widget.EditText
import com.ch.ui.R
import com.ch.ui.utils.*

class FormFiled @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FormLayout(context, attributeSet, defStyleAttr) {

    private var mDecimalDigitsInputFilter: DecimalDigitsInputFilter? = null
    private var mlengthFilter: InputFilter.LengthFilter? = null
    private var mKeyListener: KeyListener? = null
    private var maxLength: Int = -1
    private var decimalLength: Int = -1
    private var integerLength: Int = -1
    private var mInputType: Int = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
    private var mIsCanEdit = true //设置点击事件开关，用于同一个界面下点击事件的开关
    val mEtText: EditText


    init {
        inflate(context, R.layout.layout_form_filed, this)
        mEtText = findViewById(R.id.et_text)
        mKeyListener = mEtText.keyListener
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
        a.getViewInteger(R.styleable.FormFiled_android_inputType) {
            mInputType = it
            mEtText.inputType = it
        }
        a.getViewText(R.styleable.FormFiled_android_digits) {
            val digitsKeyListener = DigitsKeyListener.getInstance(it)
            mEtText.keyListener = digitsKeyListener
            mKeyListener = digitsKeyListener
        }
        updateFilter()
        a.recycle()
    }

    /** 获取内容 */
    fun getText() = mEtText.text.toString().trim()

    /** 设置是否可编辑 */
    fun setIsCanEdit(isCanEdit: Boolean) {
        mIsCanEdit = isCanEdit
        mEtText.isFocusableInTouchMode = isCanEdit
        mEtText.isFocusable = isCanEdit
        mEtText.isEnabled = isCanEdit
        // 若设置点击事件，此处进行释放
        mEtText.isLongClickable = true
        mEtText.isCursorVisible = true
        mEtText.ellipsize = if (isCanEdit) null else TextUtils.TruncateAt.END
        if (isCanEdit) mEtText.setSelection(getText().length)
        if (isCanEdit) mEtText.inputType = mInputType
        mEtText.keyListener = if (isCanEdit) mKeyListener else null
    }

    /** 设置允许编辑 */
    fun getIsCanEdit() = mIsCanEdit

    /** 设置小数点限制 */
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
        mEtText.isFocusableInTouchMode = false
        mEtText.isFocusable = false
        mEtText.isLongClickable = false
        mEtText.isEnabled = true
        mEtText.isCursorVisible = false
        mEtText.setOnClickListener { if (mIsCanEdit) block() }
        //未设置点击事件时，监听单位的点击事件，用于扩大点击事件范围
        if (!isLisUnitClick) mTvUnit.setOnClickListener { if (mIsCanEdit) block() }
    }

    fun resetTextClick() {
        mEtText.isFocusableInTouchMode = true
        mEtText.isFocusable = true
        mEtText.isLongClickable = true
        mEtText.isCursorVisible = true
    }
}