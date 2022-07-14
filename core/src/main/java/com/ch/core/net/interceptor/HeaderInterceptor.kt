package com.ch.core.net.interceptor

import com.blankj.utilcode.util.SPStaticUtils
import com.ch.core.config.HttpConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 请求参数拦截器，用来处理参数的统一封装和加密
 *
 * @author ch
 * @date 2021/3/3 9:27
 */
class HeaderInterceptor(
    private val params: () -> Map<String, String>//使用方法主要用于参数更新问题，避免固定传参导致数据无法更新
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestBuilder = originRequest.newBuilder();
        val token = SPStaticUtils.getString(HttpConfig.TOKEN)
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader(HttpConfig.AUTHORIZATION, HttpConfig.APP_BEARER + token)
        }
        val map= params()
        map.keys.forEach { key ->
            val value = map[key]
            requestBuilder.addHeader(key, value.toString())
        }
        val request = requestBuilder.build();
        return chain.proceed(request)
    }
}