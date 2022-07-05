package com.ch.ui.form

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.ch.ui.R
import com.ch.ui.utils.*
import com.ch.ui.utils.ViewExt.setLeftDrawable
import com.ch.ui.utils.ViewExt.setLeftDrawableSize
import com.ch.ui.utils.ViewExt.setLeftDrawableTint
import com.ch.ui.utils.ViewExt.setRightDrawable
import com.ch.ui.utils.ViewExt.setRightDrawableSize
import com.ch.ui.utils.ViewExt.setRightDrawableTint

open class FormLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {
    private val mRootView: ConstraintLayout
    private val flContainer: View
    private val line: View
    val mFlTitle: FormLabel
    private val mIvCenter: ImageView
    val mTvUnit: TextView
    protected var isLisUnitClick = false

    init {
        mRootView = inflate(context, R.layout.layout_form_container, null) as ConstraintLayout
        mRootView.apply {
            flContainer = findViewById(R.id.view_container)
            mFlTitle = findViewById(R.id.fl_title)
            mIvCenter = findViewById(R.id.iv_center)
            mTvUnit = findViewById(R.id.tv_unit)
            line = findViewById(R.id.line)
        }
        this.addView(mRootView)

        val a = context.obtainStyledAttributes(attributeSet, R.styleable.FormLayout)
        //=========FormLayout 标题左侧=========
        a.getViewText(R.styleable.FormLayout_leftTitle) { mFlTitle.mLeftTitle = it }
        a.getViewSp(R.styleable.FormLayout_leftTitleSize) { mFlTitle.mLeftTitleTextSize = it }
        a.getViewColor(R.styleable.FormLayout_leftTitleColor) { mFlTitle.mLeftTitleTextColor = it }
        //=========FormLayout 标题部分=========
        a.getViewText(R.styleable.FormLayout_android_title) { mFlTitle.mTitle = it }
        a.getViewSp(R.styleable.FormLayout_titleSize) { mFlTitle.mTitleTextSize = it }
        a.getViewColor(R.styleable.FormLayout_android_titleTextColor) { mFlTitle.mTitleTextColor = it }
        a.getViewDp(R.styleable.FormLayout_titleMarginLeft) { mFlTitle.mTextMarginLeft = it }
        a.getViewDp(R.styleable.FormLayout_titleMarginRight) { mFlTitle.mTextMarginRight = it }
        //=========FormLayout 标题右侧=========
        a.getViewText(R.styleable.FormLayout_rightTitle) { mFlTitle.mRightTitle = it }
        a.getViewSp(R.styleable.FormLayout_rightTitleSize) { mFlTitle.mRightTitleTextSize = it }
        a.getViewColor(R.styleable.FormLayout_rightTitleColor) { mFlTitle.mRightTitleTextColor = it }
        //=========FormLayout 单位=========
        a.getViewText(R.styleable.FormLayout_unit, this::setUnitText)
        a.getViewSp(R.styleable.FormLayout_unitTextSize, this::setUnitTextSize)
        a.getViewColor(R.styleable.FormLayout_unitTextColor, this::setUnitTextColor)
        //=========FormLayout 左側图标=========
        a.getViewDrawable(R.styleable.FormLayout_titleDrawable, this::setTitleDrawable)
        a.getViewDp(R.styleable.FormLayout_titleDrawableSize, this::setTitleDrawableSize)
        a.getViewColor(R.styleable.FormLayout_titleDrawableTint, this::setTitleDrawableTint)
        a.getViewDp(R.styleable.FormLayout_titleDrawablePadding, this::setTitleDrawablePadding)
        //=========FormLayout 中側图标=========
        a.getViewDrawable(R.styleable.FormLayout_centerDrawable, this::setCenterDrawable)
        //=========FormLayout 右側图标=========
        a.getViewDrawable(R.styleable.FormLayout_unitDrawable, this::setUnitDrawable)
        a.getViewDp(R.styleable.FormLayout_unitDrawableSize, this::setUnitDrawableSize)
        a.getViewColor(R.styleable.FormLayout_unitDrawableTint, this::setUnitDrawableTint)
        a.getViewDp(R.styleable.FormLayout_unitDrawablePadding, this::setUnitDrawablePadding)
        //=========FormLayout title 最小宽度=========
        a.getViewDp(R.styleable.FormLayout_titleMinWidth) { mFlTitle.minWidth = it }
        //=========FormLayout 分割线=========
        a.getViewBool(R.styleable.FormLayout_isShowLine) { line.visibility = if (it) View.VISIBLE else GONE }
        a.recycle()
    }

    /** Unit属性设置 */
    fun setUnitText(title: CharSequence? = null) = setUnitText(title.toString())
    fun setUnitText(title: String? = null) = run { mTvUnit.text = title ?: "" }
    fun setUnitTextColor(titleTextColor: Int) = mTvUnit.setTextColor(titleTextColor)
    fun setUnitTextSize(titleTextSize: Int) = run { mTvUnit.textSize = titleTextSize.toFloat() }

    /** 设置右侧图片 */
    fun setTitleDrawable(drawable: Drawable?) = mFlTitle.setLeftDrawable(drawable)
    fun setTitleDrawableSize(size: Int) = mFlTitle.setLeftDrawableSize(size)
    fun setTitleDrawableTint(color: Int) = mFlTitle.setLeftDrawableTint(color)
    fun setTitleDrawablePadding(padding: Int) = kotlin.run { mFlTitle.compoundDrawablePadding = padding }

    /** 设置中间图片 */
    fun setCenterDrawable(drawable: Drawable?) = kotlin.run { mIvCenter.background = drawable }

    /** 设置右侧图片 */
    fun setUnitDrawable(drawable: Drawable?) = mTvUnit.setRightDrawable(drawable)
    fun setUnitDrawableSize(size: Int) = mTvUnit.setRightDrawableSize(size)
    fun setUnitDrawableTint(color: Int) = mTvUnit.setRightDrawableTint(color)
    fun setUnitDrawablePadding(padding: Int) = kotlin.run { mTvUnit.compoundDrawablePadding = padding }

    fun setUnitClick(block: () -> Unit) {
        isLisUnitClick = true
        mTvUnit.setOnClickListener { block() }
    }

    fun setIvCenterClick(block: () -> Unit) {
        mIvCenter.setOnClickListener { block() }
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