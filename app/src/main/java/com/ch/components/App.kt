package com.ch.components

import android.app.Application
import com.ch.ui.select.PictureView

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        PictureView.globalViewsAdapter = PicAdapter()
    }
}