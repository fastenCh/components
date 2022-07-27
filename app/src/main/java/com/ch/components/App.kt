package com.ch.components

import android.app.Application
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.ch.dokit_ext.DokitUtils
import com.ch.jpush.JpushUtils
import com.ch.ui.TitleBar
import com.ch.ui.select.PictureView

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        PictureView.globalViewsAdapter = PicAdapter()
//        TitleBar.mTextColor = ContextCompat.getColor(this, com.ch.ui.R.color.font_333)
        JpushUtils.init(this,true)
        DokitUtils.init(this,"3518bdb66a7a8dfc84c48e02c6788bdf")
    }
}