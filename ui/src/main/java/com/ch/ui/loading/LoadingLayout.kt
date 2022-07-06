package com.ch.ui.loading

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.fastench.ui.loading.LoadView
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * Created by ch on 2021/5/28.
 * 自定义统一的缺省页面。
 */
class LoadingLayout @JvmOverloads constructor(
    private val context: Context,
    private val contentView: View,
    private val listener: ReloadListener? = null
) {

    private var mEmptyViewMap = mutableMapOf<String, LoadView>()
    private var loadFrameLayout: FrameLayout = FrameLayout(context)

    companion object {
        var globalViews: () -> MutableMap<LoadingType, out LoadView>? = { null }
        var otherViews: () -> MutableMap<String, out LoadView>? = { null }

        /**
         * 全局初始化方法,不通过参数传入主要用于避免VIew唯一性导致监听参数出错
         *
         * @param  globalViews 全局view（LoadingType -> LoadView）
         * @param  otherViews 其他自定义view（flag -> LoadView）
         */
        fun init(
            globalViews: () -> MutableMap<LoadingType, out LoadView>?,
            otherViews: () -> MutableMap<String, out LoadView>?
        ) {
            Companion.globalViews = globalViews
            Companion.otherViews = otherViews
        }
    }

    init {
        //1、创造我们的Loading布局，主要用于承载原始布局和空布局使用
        val globalViewsMap = globalViews()
        globalViewsMap?.forEach {
            mEmptyViewMap[it.key.name] = it.value.attach(context, listener)
        }
        val otherViewsMap = otherViews()
        otherViewsMap?.forEach {
            addEmptyView(it.key, it.value)
        }
        //2、获取原始布局所在位置，并移除原始布局
        val parentGroup = contentView.parent
        if (parentGroup is SmartRefreshLayout) {
            parentGroup.removeView(contentView)
            //默认显示新布局
            showView(contentView)
            parentGroup.setRefreshContent(loadFrameLayout)
        } else if (parentGroup is ViewGroup) {
            val layoutParams = contentView.layoutParams
            parentGroup.removeView(contentView)
            //默认显示新布局
            showView(contentView)
            val childIndex = parentGroup.indexOfChild(contentView)
            parentGroup.addView(loadFrameLayout, childIndex, layoutParams)
        }
    }

    /**
     * 用于全局初始化空View
     */
    fun initEmptyView(emptyLoadingMap: MutableMap<LoadingType, out LoadView>?) {
        emptyLoadingMap?.forEach {
            mEmptyViewMap[it.key.name] = it.value.attach(context, listener)
        }
    }

    /**
     * 用于全局初始化空View
     * @param flag 标识
     * @param loadView 缺省页面
     */
    fun addEmptyView(flag: String, loadView: LoadView) {
        //为避免不可预计错误，flag不允许重复
        if (mEmptyViewMap.containsKey(flag)) {
            throw Exception("Loading View cannot same index")
        }
        mEmptyViewMap[flag] = loadView.attach(context, listener)
    }

    /**
     * 用于全局初始化空View
     * @param loadingType 标识
     * @param loadView 缺省页面
     */
    fun addEmptyView(loadingType: LoadingType, loadView: LoadView) {
        addEmptyView(loadingType.name, loadView)
    }

    /**
     * 用于显示content布局
     * @param view 缺省页面
     */
    private fun showView(view: View) {
        loadFrameLayout.removeAllViews()
        loadFrameLayout.addView(
            view, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    /**
     * 用于显示界面中独立的View,即自定义需要展示的界面
     */
    fun showView(flag: String) {
        if (mEmptyViewMap.containsKey(flag)) {
            mEmptyViewMap[flag]?.getView()?.let { showView(it) }
        }
    }

    /**
     * 用于显示Content布局，即：原始布局
     */
    fun showContent() = showView(contentView)
    fun showLoading() = showView(LoadingType.LOADING)
    fun showError() = showView(LoadingType.ERROR)
    fun showEmpty() = showView(LoadingType.EMPTY)
    fun showOther() = showView(LoadingType.OTHER)
    fun showNetError() = showView(LoadingType.NET_ERROR)

    private fun showView(loadingType: LoadingType) = showView(loadingType.name)

}

enum class LoadingType {
    LOADING, CONTENT, ERROR, NET_ERROR, EMPTY, OTHER
}

interface ReloadListener {
    fun onReload(view: View)
}
