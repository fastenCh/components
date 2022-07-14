package com.ch.core.net

import com.ch.core.config.HttpConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * retrofit实例
 *
 * @author ch
 * @date 2021/3/2 16:27
 */

class HttpApi {

    companion object {
        /**
         * @param baseUrl API地址
         * @param interceptors 拦截器，以内置拦截器，直接使用
         */
        fun buildRetrofit(baseUrl: String, interceptors: MutableList<Interceptor>, factory: Converter.Factory): Retrofit {
            val okHttpClientBuilder = OkHttpClient.Builder()
                .readTimeout(HttpConfig.READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(HttpConfig.WRITE_TIME, TimeUnit.SECONDS)
                .connectTimeout(HttpConfig.CONNECT_TIME, TimeUnit.SECONDS)
                .addNetworkInterceptor(
                    //输出请求日志
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            interceptors.forEach {
                okHttpClientBuilder.addInterceptor(it)
            }
            return Retrofit.Builder()
                .client(okHttpClientBuilder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(factory)
                .baseUrl(baseUrl)
                .build()
        }
    }
}