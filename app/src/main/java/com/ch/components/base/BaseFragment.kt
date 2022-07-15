package com.ch.components.base

import androidx.databinding.ViewDataBinding
import com.ch.core.base.BaseCoreFragment

abstract class BaseFragment<VDB:ViewDataBinding>: BaseCoreFragment<VDB>() {
    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}