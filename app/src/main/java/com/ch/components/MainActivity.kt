package com.ch.components

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.ch.components.base.BaseActivity
import com.ch.components.databinding.ActivityMainBinding
import com.ch.components.vm.MainViewModel
import com.ch.core.ktx.show
import com.ch.ui.form.FormFiled

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_main
    private val mState by viewModels<MainViewModel>()

    override fun bindView() {
        mBinding.vm = mState
        mBinding.click = ClickProxy()
    }

    override fun initData(savedInstanceState: Bundle?) {
        val ff = findViewById<FormFiled>(R.id.ff);
        ff.setTextClickCenter {
            Toast.makeText(this, "AAAA", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btn).setOnClickListener {
            ff.setIsCanEdit(!ff.getIsCanEdit())
        }
        "测速".show()
    }

    inner class ClickProxy {

    }
}