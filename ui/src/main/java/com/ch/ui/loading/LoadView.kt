package com.fastench.ui.loading

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.Keep
import androidx.annotation.LayoutRes
import com.ch.ui.loading.ReloadListener
import java.lang.Exception

/**
 * 缺省页面
 * Created by ch on 2021/3/25.
 */
@Keep
abstract class LoadView : ILoadView {

    protected lateinit var mContext: Context
    private lateinit var mView: View
    private var mListener: ReloadListener? = null

    fun attach(context: Context, listener: ReloadListener?): LoadView {
        mContext = context
        this.mListener = listener
        val createView = createView(mContext)
        val layoutRes = getLayoutRes()
        if (createView == null && layoutRes == -1) {
            throw Exception("you must call createView or getLayoutRes method init view")
        }
        mView = createView ?: LayoutInflater.from(context).inflate(layoutRes, null, false)
        onViewCreate(mView)
        return this
    }

    /**
     * create创建完成的回调事件
     */
    override fun onViewCreate(view: View) {
        //点击缺省页触发重新加载操作
        view.setOnClickListener {
            if (isEnableReload()) {
                onReload()
            }
        }
    }

    /**
     * 重新加载事件
     */
    protected fun onReload() {
        mListener?.onReload(mView)
    }

    /**
     * 创建View，可重在以下两者方法中的一种
     */
    override fun createView(context: Context): View? {
        return null
    }

    override fun getLayoutRes(): Int = -1

    override fun isEnableReload(): Boolean = true

    /**
     * 获取View
     */
    fun getView(): View {
        return mView
    }
}

interface ILoadView {
    /**
     * 创建View
     */
    fun createView(context: Context): View?

    /**
     * 获取布局ID
     */
    @LayoutRes
    fun getLayoutRes(): Int

    /**
     * View创建成功回调
     */
    fun onViewCreate(view: View)

    /**
     * 是否开启刷新
     */
    fun isEnableReload(): Boolean
}