package com.ch.core.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ViewModelScope {
    private var mFragmentProvider: ViewModelProvider? = null
    private var mActivityProvider: ViewModelProvider? = null
    private var mApplicationProvider: ViewModelProvider? = null

    fun <T : ViewModel> getFragmentScopeViewModel(fragment: Fragment, modelClass: Class<T>): T {
        if (mFragmentProvider == null) {
            mFragmentProvider = ViewModelProvider(fragment)
        }
        return mFragmentProvider!![modelClass]
    }

    fun <T : ViewModel> getActivityScopeViewModel(activity: AppCompatActivity, modelClass: Class<T>): T {
        if (mActivityProvider == null) {
            mActivityProvider = ViewModelProvider(activity)
        }
        return mActivityProvider!![modelClass]
    }

    fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T {
        if (mApplicationProvider == null) {
            mApplicationProvider = ViewModelProvider(ApplicationInstance.getInstance())
        }
        return mApplicationProvider!![modelClass]
    }
}