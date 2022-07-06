package com.ch.ui

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.ch.ui.utils.SpanUtils
import kotlin.math.roundToInt

/**
 * 倒计时文本组件
 * @author ch
 * @date 2022年7月6日14:04:47
 */
class CountDownTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var mDownTimer: CountDownTimer? = null

    fun start(
        millisecond: Long, timeConfigs: MutableList<TimeConfig> = mutableListOf(TimeConfig.H, TimeConfig.M, TimeConfig.S),
        color: Int = textColors.defaultColor,
        startText: String = "",
        endText: String = "",
        textColor: Int = ContextCompat.getColor(context, R.color.font_666),
    ) {
        mDownTimer?.cancel()
        mDownTimer = object : CountDownTimer(millisecond, 1000L) {

            override fun onTick(time: Long) {
                // 计算天
                val days = if (timeConfigs.contains(TimeConfig.D)) time / (1000 * 60 * 60 * 24) else 0
                // 计算小时
                val hours = if (timeConfigs.contains(TimeConfig.H)) (time - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60) else 0
                // 计算分
                val minutes = if (timeConfigs.contains(TimeConfig.M)) (time - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60) else 0
                //计算秒，并优化
                val seconds = if (timeConfigs.contains(TimeConfig.S))
                    ((time - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000).toDouble().roundToInt() else 0
                val span = SpanUtils.with(this@CountDownTextView)
                span.append(startText).setForegroundColor(textColor)
                if (timeConfigs.contains(TimeConfig.D)) span.append(days.toString()).setForegroundColor(color).append("天").setForegroundColor(textColor)
                if (timeConfigs.contains(TimeConfig.H)) span.append((hours.toString())).setForegroundColor(color).append("时").setForegroundColor(textColor)
                if (timeConfigs.contains(TimeConfig.M)) span.append(minutes.toString()).setForegroundColor(color).append("分").setForegroundColor(textColor)
                if (timeConfigs.contains(TimeConfig.S)) span.append(seconds.toString()).setForegroundColor(color).append("秒").setForegroundColor(textColor)
                span.append(endText).setForegroundColor(textColor)
                span.create()
            }

            override fun onFinish() {
            }
        }
        mDownTimer?.start()
    }

    fun cancel(){
        mDownTimer?.cancel()
    }

    enum class TimeConfig {
        D, H, M, S
    }
}
