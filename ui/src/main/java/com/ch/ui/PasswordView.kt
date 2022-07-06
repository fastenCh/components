package com.ch.ui

import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import android.graphics.RectF
import android.text.InputFilter.LengthFilter
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.ch.ui.R
import android.text.TextWatcher
import android.text.Editable
import android.text.InputFilter
import android.util.AttributeSet
import com.ch.ui.form.FormLayout

/**
 * 带边框密码输入框
 * @author ch
 * @date 2022年7月6日14:04:47
 */
class PasswordView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attributeSet, defStyleAttr) {

    /**
     * 绘制EditText的宽高
     */
    private var mHeight = 0
    private var mWidth = 0

    /**
     * 分割线宽度，用来计算分割点的坐标
     */
    private var divingWidth = 0
    private var count = 6

    /** 输入位置 */
    private var position = 0

    /** 密码框中第一个黑色圆心坐标 */
    private var startX = 0
    private var startY = 0

    /** 密码圆半径 */
    private var radius = 12
    private val rectF = RectF()

    /** 绘制宽度 */
    private var borderWidth = 2
    private var lineWidth = 1

    /** 输入框圆角 */
    private var roundAngle = 10

    /** 边框、分割线、圆心的画笔 */
    private var borderPaint: Paint? = null
    private var divingPaint: Paint? = null
    private var circlePaint: Paint? = null

    /** 绘制颜色，画笔颜色 */
    private var borderColor = Color.GRAY
    private var divingColor = Color.GRAY
    private var circleColor = Color.BLACK

    init {
        // 获取配置属性
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.PasswordView)
        count = typedArray.getInt(R.styleable.PasswordView_inputMaxNum, count)
        roundAngle = typedArray.getDimensionPixelOffset(R.styleable.PasswordView_roundAngle, roundAngle)
        borderColor = typedArray.getColor(R.styleable.PasswordView_borderColor, borderColor)
        divingColor = typedArray.getColor(R.styleable.PasswordView_borderColor, divingColor)
        circleColor = typedArray.getColor(R.styleable.PasswordView_circleColor, circleColor)
        borderWidth = typedArray.getDimensionPixelOffset(R.styleable.PasswordView_borderWidth, borderWidth)
        lineWidth = typedArray.getDimensionPixelOffset(R.styleable.PasswordView_divingWidth, lineWidth)
        radius = typedArray.getDimensionPixelOffset(R.styleable.PasswordView_circleRadius, radius)
        typedArray.recycle()
        initPaint()
        this.filters = arrayOf<InputFilter>(LengthFilter(count))
        this.isCursorVisible = false
    }

    /**
     * Initial Paint
     */
    private fun initPaint() {
        // 抗锯齿
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint!!.isAntiAlias = true
        // 设置画笔宽度
        borderPaint!!.strokeWidth = borderWidth.toFloat()
        // 设置画笔风格
        borderPaint!!.style = Paint.Style.STROKE
        // 设置画笔颜色
        borderPaint!!.color = borderColor
        // 分割线画笔
        divingPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        divingPaint!!.isAntiAlias = true
        divingPaint!!.strokeWidth = lineWidth.toFloat()
        divingPaint!!.color = divingColor
        divingPaint!!.style = Paint.Style.FILL
        // 圆心画笔
        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint!!.isAntiAlias = true
        circlePaint!!.style = Paint.Style.FILL
        circlePaint!!.color = circleColor
        circlePaint!!.strokeWidth = radius.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 获取宽高坐标等数据
        mHeight = h
        mWidth = w
        // 分割线宽度
        divingWidth = w / count
        // 密码黑色圆坐标
        startX = w / count / 2
        startY = h / 2
        rectF[0f, 0f, w.toFloat()] = h.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        // 绘制圆角矩形
        canvas.drawRoundRect(rectF, roundAngle.toFloat(), roundAngle.toFloat(), borderPaint!!)
        // 绘制分割线，5个分割线
        for (i in 1 until count) {
            canvas.drawLine((divingWidth * i).toFloat(), 0f, (divingWidth * i).toFloat(), mHeight.toFloat(), divingPaint!!)
        }
        for (i in 0 until position) {
            canvas.drawCircle((startX * (2 * i + 1)).toFloat(), startY.toFloat(), radius.toFloat(), circlePaint!!)
        }
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        // 获取当前输入位置
        position = text.length
    }

    private fun setOnFinishListener(block: () -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (text.toString().length == count) {
                    block()
                }
            }
        })
    }
}