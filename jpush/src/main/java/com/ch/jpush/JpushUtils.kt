package com.ch.jpush

import android.content.Context
import cn.jpush.android.api.JPushInterface

object JpushUtils {

    /**
     * 初始化方法
     * @param context Context
     * @param isDebug Boolean
     *
     * defaultConfig {
         manifestPlaceholders = [
            JPUSH_PKGNAME : applicationId,
            JPUSH_APPKEY : "你的 Appkey ",
            JPUSH_CHANNEL : "developer-default"
        ]
     * }
     */
    fun init(context: Context, isDebug: Boolean = false) {
        JPushInterface.setDebugMode(isDebug);
        JPushInterface.init(context);
    }
}