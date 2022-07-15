package com.ch.core.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ch.core.ktx.show
import com.kunminx.architecture.ui.callback.UnPeekLiveData

abstract class BaseCoreActivity<VDB : ViewDataBinding> : AppCompatActivity(), IView {

    protected lateinit var mBinding: VDB
    private val mViewModelScope = ViewModelScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.lifecycleOwner = this
        bindView()
        subscribeUi()
        initData(savedInstanceState)
    }

    override fun showMsg(msg: String)  = msg.show()

    override fun showMsg(msgResId: Int) = msgResId.show()

    override fun subscribeUi() {}

    protected open fun <T : ViewModel> getActivityScopeViewModel(modelClass: Class<T>): T {
        return mViewModelScope.getActivityScopeViewModel(this, modelClass)
    }

    protected open fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T {
        return mViewModelScope.getApplicationScopeViewModel(modelClass)
    }

    protected fun <T> UnPeekLiveData<T>.observerKt(block: (T) -> Unit) {
        this.observeStickyForever { block(it) }
    }

    protected fun <T> MutableLiveData<T>.observerKt(block: (T) -> Unit) {
        this.observe(this@BaseCoreActivity) { block(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mBinding.isInitialized) mBinding.unbind()
    }

}