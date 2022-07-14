package com.ch.core.net.interceptor

import com.blankj.utilcode.util.GsonUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLDecoder


/**
 * @description
 * @author ch
 * @date 2021/6/28
 * @property commonParams 放置公共参数
 * @property paramsInterceptor 参数拦截器，用于处理动态添加参数
 * @property urlInterceptor url拦截器，用于处理动态添加URl
 * @property isFormToJsonBody 是否将表单形式请求转为application/json方式请求
 * @property isJsonToFormBody 是否将application/json方式请求转为表单形式请求
 */
class RequestInterceptor(
    private val commonParams: () -> Map<String, String> = { emptyMap() },
    private val paramsInterceptor: (data: Map<String, String>) -> MutableMap<String, String> = { it.toMutableMap() },
    private val urlInterceptor: (data: Map<String, String>) -> Map<String, String> = { emptyMap() },
    private val isFormToJsonBody: Boolean = false,
    private val isJsonToFormBody: Boolean = false,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newRequestBuilder = original.newBuilder()
        var data = mutableMapOf<String, String>()// 数据集合
        val commonParams = commonParams()
        if (original.method == "POST") {
            when (original.body) {
                is FormBody -> {
                    data.putAll(parseFormBody(original.body as FormBody))
                    data.putAll(commonParams)
                    data = paramsInterceptor(data)
                    newRequestBuilder.url(buildHttpUrl(original, urlInterceptor(data) as MutableMap<String, String>))
                    if (isFormToJsonBody) {
                        buildRequestBodyBuilder(newRequestBuilder, data)
                    } else {
                        buildFormBodyBuilder(newRequestBuilder, data)
                    }
                }
                is RequestBody -> {
                    data.putAll(parseRequestBody(original.body as MultipartBody))
                    data.putAll(commonParams)
                    data = paramsInterceptor(data)
                    newRequestBuilder.url(buildHttpUrl(original, urlInterceptor(data) as MutableMap<String, String>))
                    if (isJsonToFormBody) {
                        buildFormBodyBuilder(newRequestBuilder, data)
                    } else {
                        buildRequestBodyBuilder(newRequestBuilder, data)
                    }
                }
                is MultipartBody -> {
                    val multipartBody = parseMultipartBody(original.body as MultipartBody)
                    addFormatData(multipartBody, commonParams)
                    newRequestBuilder.method(original.method, multipartBody.build())
                }
            }
            return chain.proceed(newRequestBuilder.build())
        } else {
            val urlBuilder = newRequestBuilder.url(buildHttpUrl(original, urlInterceptor(data) as MutableMap<String, String>))
            return chain.proceed(urlBuilder.build())
        }
    }
}

private fun parseFormBody(body: FormBody): MutableMap<String, String> {
    val data = mutableMapOf<String, String>()
    for (i in 0 until body.size) {
        val key = body.encodedName(i)
        data[key] = URLDecoder.decode(body.encodedValue(i), "UTF-8")
    }
    return data
}

private fun parseMultipartBody(body: MultipartBody): MultipartBody.Builder {
    val newMultipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
    for (i in 0 until body.size) {
        newMultipartBody.addPart(body.part(i))
    }
    return newMultipartBody;
}

private fun addFormatData(body: MultipartBody.Builder, data: Map<String, String>) {
    if (data.isNotEmpty()) {
        data.iterator().forEach { body.addFormDataPart(it.key, it.value) }
    }
}

private fun parseRequestBody(body: RequestBody): MutableMap<String, String> {
    var data = mutableMapOf<String, String>()
    val buffer = okio.Buffer()
    body.writeTo(buffer)
    val bodyStr = buffer.readUtf8()
    if (bodyStr.isNotEmpty()) {
        data = GsonUtils.fromJson<Map<String, String>>(bodyStr, Map::class.java) as MutableMap<String, String>
    }
    return data;
}


private fun buildHttpUrl(request: Request, map: MutableMap<String, String>): HttpUrl {
    val newGetUrlBuilder = request.url.newBuilder()
    map.iterator().forEach {
        newGetUrlBuilder.addQueryParameter(it.key, it.value)
    }
    return newGetUrlBuilder.build()
}

fun buildFormBodyBuilder(builder: Request.Builder, data: Map<String, String>) {
    val newFormBody = FormBody.Builder()
    data.iterator().forEach { newFormBody.add(it.key, it.value) }
    builder.method("POST", newFormBody.build())
}

/**
 * 创建RequestBody
 * @param builder Builder
 * @param data Map<String, String>
 */
fun buildRequestBodyBuilder(builder: Request.Builder, data: Map<String, String>) {
    val newRequestBody: RequestBody = GsonUtils.toJson(data).toRequestBody("application/json".toMediaTypeOrNull())
    builder.method("POST", newRequestBody)
}