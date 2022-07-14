package com.ch.core.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import org.jetbrains.annotations.NotNull

/**
 * @author ch
 * @date 2022年7月13日11:02:19
 */
interface IView {
    /**
     * 获取根布局Id
     */
    @LayoutRes
    @NotNull
    fun getLayoutId(): Int

    /**
     *  注册DataBinding事件
     */
    fun bindView()

    /**
     *  初始化数据
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 统一处理订阅UI
     */
    fun subscribeUi();

    /**
     * 显示消息
     * @param msg 消息
     */
    fun showMsg(msg: String)

    /**
     * 显示消息
     * @param msgResId 消息ID
     */
    fun showMsg(@StringRes msgResId: Int)

    /**
     * 显示加载框
     */
    fun showLoading()

    /**
     * 隐藏加载框
     */
    fun hideLoading()
}