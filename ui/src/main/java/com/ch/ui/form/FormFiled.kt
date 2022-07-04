package com.ch.ui.form

import android.content.Context
import android.util.AttributeSet
import com.ch.ui.R

class FormFiled @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FormLayout(context, attributeSet, defStyleAttr) {

    init {
        inflate(context, R.layout.layout_form_filed, this)
    }
}