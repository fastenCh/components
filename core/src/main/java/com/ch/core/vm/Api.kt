package com.ch.core.vm

import androidx.annotation.Keep
import com.ch.core.vm.BaseCoreViewModel

@Keep
open class Api(
    open var code: String? = null,//错误码
    open var msg: String? = null,//错误信息
) {
    /** 请求是否成功 */
    open fun isSuccess(): Boolean = code != null && BaseCoreViewModel.successCode == code
}