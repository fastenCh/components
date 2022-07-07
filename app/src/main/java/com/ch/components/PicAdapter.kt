package com.ch.components

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ch.ui.select.PictureAdapter
import com.ch.ui.select.PictureView
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener


class PicAdapter : PictureAdapter<PictureView.PictureBean<*>>() {
    override fun loadPic(imageView: ImageView, url: String, data: PictureView.PictureBean<*>) {
        Glide.with(mContext).load(url).fitCenter().centerCrop().into(imageView)
    }

    override fun showPic(imageView: ImageView, url: String, data: PictureView.PictureBean<*>) {
    }

    override fun getUrl(data: PictureView.PictureBean<*>): String {
        if (data.data is LocalMedia) {
            return (data.data as LocalMedia).path
        }
        return ""
    }

    override fun onClickAdd() {
        PictureSelector.create(mContext as AppCompatActivity)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>) {
                    for (localMedia in result) {
                        val picBean: PictureView.PictureBean<LocalMedia> = PictureView.PictureBean()
                        picBean.data = localMedia
                        addPic(picBean)
                    }
                }

                override fun onCancel() {}
            })
    }
}