package com.ch.dokit_ext

import android.content.Context
import android.widget.Toast
import com.didichuxing.doraemonkit.kit.AbstractKit
import com.didichuxing.doraemonkit.kit.Category
import com.didichuxing.doraemonkit.util.ToastUtils
import java.util.*

class HostDokit : AbstractKit() {
    override val category: Int
        get() = Category.BIZ
    override val icon: Int
        get() = R.drawable.ic_host
    override val name: Int
        get() = R.string.dokit_host

    override fun onAppInit(context: Context?) {
        ToastUtils.showShort("你好")
    }
}