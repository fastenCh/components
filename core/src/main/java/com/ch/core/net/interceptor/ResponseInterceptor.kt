package com.ch.core.net.interceptor

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.ch.core.config.HttpConfig
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * 请求结果拦截器，用来处理token和加解密
 * @author ch
 * @date 2021/3/3 10:12
 */
class ResponseInterceptor(private val  block: suspend () -> String?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var response = chain.proceed(originalRequest)
        //处理token过期的情况
        if (response.code == 401) {
            runBlocking {
                launch {
                    kotlin.runCatching {
                        //此处需要获取token，可通过refreshToken或重登陆
                        block.invoke()
                    }.onSuccess {token->
                        if (!token.isNullOrBlank()) {
                            //Token自动续期
                            SPStaticUtils.put(HttpConfig.TOKEN, token)
                            val newRequest = chain.request()
                                .newBuilder()
                                .header(HttpConfig.AUTHORIZATION, HttpConfig.APP_BEARER + token)
                                .build();
                            //重新请求
                            response = chain.proceed(newRequest)
                        }else{
                            ToastUtils.showShort("用户登录过期，请重新登录")
                            SPStaticUtils.clear()
                            AppUtils.relaunchApp()
                        }
                    }.onFailure {
                        ToastUtils.showShort("用户登录过期，请重新登录")
                        SPStaticUtils.clear()
                        AppUtils.relaunchApp()
                    }
                }
            }
        }
        return response;
    }
}