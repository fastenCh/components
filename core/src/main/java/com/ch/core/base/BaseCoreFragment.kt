package com.ch.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ch.core.ktx.show
import com.kunminx.architecture.ui.callback.UnPeekLiveData

abstract class BaseCoreFragment<VDB : ViewDataBinding> : Fragment(), IView {
    private val mViewModelScope = ViewModelScope()

    protected lateinit var mActivity: AppCompatActivity
    protected lateinit var mBinding: VDB

    private var isGetData = false
    private var isLoaded = false
    private var isVisibleToUser = false
    private var isCallResume = false
    private var isCallUserVisibleHint = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isVisibleToUser = !hidden
        judgeLazyInit()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        isCallUserVisibleHint = true
        judgeLazyInit()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        isGetData = enter && !isGetData
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onResume() {
        if (!isGetData) {
            onResumeGetData()
            isGetData = true
        }
        isCallResume = true
        if (!isCallUserVisibleHint) isVisibleToUser = !isHidden
        judgeLazyInit()
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate<VDB>(inflater, getLayoutId(), container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = this
        bindView()
        subscribeUi()
        initData(savedInstanceState)
    }

    protected open fun lazyInit() {}
    protected open fun onResumeGetData() {}

    private fun judgeLazyInit() {
        if (!isLoaded && isVisibleToUser && isCallResume) {
            lazyInit()
            isLoaded = true
        }
    }

    override fun showMsg(msg: String) = msg.show()

    override fun showMsg(msgResId: Int) = msgResId.show()

    protected fun <T : ViewModel> getFragmentScopeViewModel(modelClass: Class<T>): T {
        return mViewModelScope.getFragmentScopeViewModel(this, modelClass)
    }

    protected fun <T : ViewModel> getActivityScopeViewModel(modelClass: Class<T>): T {
        return mViewModelScope.getActivityScopeViewModel(mActivity, modelClass)
    }

    protected fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T {
        return mViewModelScope.getApplicationScopeViewModel(modelClass)
    }

    protected fun <T> UnPeekLiveData<T>.observerKt(block: (T) -> Unit) {
        this.observeStickyForever { block(it) }
    }

    protected fun <T> MutableLiveData<T>.observerKt(block: (T) -> Unit) {
        this.observe(viewLifecycleOwner) { block(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mBinding.isInitialized) mBinding.unbind()
    }
}