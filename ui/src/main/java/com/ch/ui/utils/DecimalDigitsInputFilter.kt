package com.ch.ui.utils

import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned

class DecimalDigitsInputFilter : InputFilter {
    private val TAG = "MoneyValueFilter"
    lateinit var regex: Regex

    fun setDigits(digits: Int): DecimalDigitsInputFilter {
        regex = "[0-9]+(\\.[0-9]{0,${digits}})?".toRegex()
        return this
    }

    fun setDigits(integerLength: Int, digits: Int): DecimalDigitsInputFilter {
        regex = "[0-9]{1,${integerLength}}(\\.[0-9]{0,${digits}})?".toRegex()
        return this
    }

    //说的已经很明白了，return的字符串使用来替换当前的操作的，这个时候如果返回空字符串，则表示使用空字符替换当前的输入，返回 null 则表示认同当前的操作，可以认为是不做任何处理。
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
        //直接输入"."返回"0."
        //".x"删除"x"输出为"."，inputFilter无法处理成"0."，所以只处理直接输入"."的case
        if ("." == source && "" == dest.toString()) return "0."
        val builder = StringBuilder(dest);
        if ("" == source) {
            builder.replace(dstart, dend, "");
        } else {
            builder.insert(dstart, source);
        }
        val resultTemp = builder.toString();
        //判断修改后的数字是否满足小数格式，不满足则返回 "",不允许修改
        if (!regex.matches(resultTemp)) {
            if (source.isEmpty() && dend > dstart) {
                return dest.subSequence(dstart, dend)
            } else {
                return ""
            }
        }
        //若数据为01.12这样的数字，返回""
        if ("^0[0-9].*$".toRegex().matches(resultTemp)) {
            return ""
        }
        return SpannableStringBuilder(source, start, end)
    }
}