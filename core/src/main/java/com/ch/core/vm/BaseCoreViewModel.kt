package com.ch.core.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException

open class BaseCoreViewModel : ViewModel() {

    companion object {
        var successCode = "200"
    }

    /**页面加载状态*/
    val loadLiveData = UnPeekLiveData<LoadType>()

    /**
     * 统一数据请求入口，需手动处理数据为空时的情况
     * @param request 请求
     * @param success 请求成功回调
     * @param error 请求失败回调
     * @param isShowSuccess 是否显示成功回调，主要用于处理连续请求数据时，loading显示问题
     * @param isLoading 是否监听Loading
     */
    fun <T> ViewModel.request(
        request: suspend () -> T,
        success: (T) -> Unit,
        error: (msg: String) -> Unit,
        isShowSuccess: Boolean = true,
        isLoading: Boolean = true,
    ) {
        if (!NetworkUtils.isConnected()) {
            if (isLoading) loadLiveData.value = LoadType.NET_ERROR
            error("网络连接异常，请检查当前网络是否正常连接！")
            return
        }
        viewModelScope.launch {
            kotlin.runCatching {
                if (isLoading) loadLiveData.value = LoadType.LOADING
                request()
            }.onSuccess {
                //自定义处理方案
                interceptor(it)
                try {
                    if (it is Api) {
                        if (it.code == successCode) {
                            success(it)
                            if (isLoading && isShowSuccess) loadLiveData.value = LoadType.SUCCESS
                        } else {
                            error(it.msg ?: "数据请求失败,请稍后再试！")
                            if (isLoading) loadLiveData.value = LoadType.ERROR
                        }
                    } else {
                        success(it)
                        if (isLoading && isShowSuccess) loadLiveData.value = LoadType.SUCCESS
                    }
                } catch (e: Exception) {
                    error("数据解析异常，请稍后重试")
                    if (isLoading) loadLiveData.value = LoadType.ERROR
                    e.printStackTrace()
                }
            }.onFailure {
                val msg = when (it) {
                    is ConnectException -> "网络连接错误，请稍后重试！"
                    is SocketTimeoutException -> "服务连接超时，请稍后重试！"
                    else -> "未知异常：" + it.message
                }
                error(msg)
                if (isLoading) loadLiveData.value = LoadType.ERROR
                it.printStackTrace()
            }
        }
    }

    protected open fun <T> interceptor(it: T) {

    }
}

enum class LoadType {
    ERROR,
    LOADING,
    SUCCESS,
    NET_ERROR
}