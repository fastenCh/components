package com.ch.components.base

import androidx.databinding.ViewDataBinding
import com.ch.core.base.BaseCoreActivity

abstract class BaseActivity<VDM:ViewDataBinding>: BaseCoreActivity<VDM>() {
    override fun hideLoading() {
    }

    override fun showLoading() {
    }
}