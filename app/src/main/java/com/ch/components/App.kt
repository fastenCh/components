package com.ch.components

import android.app.Application
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.ch.ui.TitleBar
import com.ch.ui.select.PictureView

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        PictureView.globalViewsAdapter = PicAdapter()
//        TitleBar.mTextColor = ContextCompat.getColor(this, com.ch.ui.R.color.font_333)
    }
}