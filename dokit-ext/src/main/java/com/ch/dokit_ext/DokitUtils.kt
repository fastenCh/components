package com.ch.dokit_ext

import android.app.Application
import com.didichuxing.doraemonkit.DoKit
import com.didichuxing.doraemonkit.config.GpsMockConfig

object DokitUtils {

    fun init(application: Application, productId: String? = null) {
        GpsMockConfig.setGPSMockOpen(false)
        val builder = DoKit.Builder(application)
        if (productId != null) builder.productId(productId)
        builder.customKits(mutableListOf(HostDokit()))
        builder.build()
    }
}