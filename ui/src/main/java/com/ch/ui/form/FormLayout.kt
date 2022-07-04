package com.ch.ui.form

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.ch.ui.R

open class FormLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {
    private val mRootView: ConstraintLayout
    private val flContainer: FrameLayout
    val mTvTitle: TextView
    val mTvUnit: TextView

    init {
        mRootView = inflate(context, R.layout.layout_form_container, null) as ConstraintLayout
        mRootView.apply {
            flContainer = findViewById(R.id.fl_container)
            mTvTitle = findViewById(R.id.tv_title)
            mTvUnit = findViewById(R.id.tv_unit)
        }
        this.addView(mRootView)

        context.obtainStyledAttributes(R.styleable.FormLabel)
    }

    open fun setUnit(unit: String) {
        mTvUnit.setText(unit)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        requestView()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun requestView() {
        if (children.count() > 1) {
            val childAt = getChildAt(1)
            this.removeView(childAt)
            val layoutParams = flContainer.layoutParams
            mRootView.removeView(flContainer)
            mRootView.addView(childAt, layoutParams)
        }
    }
}