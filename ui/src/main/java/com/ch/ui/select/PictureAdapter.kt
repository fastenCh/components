package com.ch.ui.select

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ch.ui.R

/**
 * 图片选择组件适配器，子类需实现此类
 * @author ch
 */
abstract class PictureAdapter<T> : RecyclerView.Adapter<PictureAdapter.PicViewHolder>() {
    protected lateinit var mContext: Context
    protected var mSelectStyle: Int = 1
    protected var mCustomParam1: Int = 1;
    protected var mCustomParam2 = 1;
    protected var mCustomParam3: Int = 1;
    protected var mMaxCount: Int = 3
    protected var mIsPreview: Boolean = false
    var mDataList = mutableListOf<PictureView.PictureBean<*>>()
    var mBlock: (pictureBean: PictureView.PictureBean<*>) -> PictureView.PictureBean<*>? = { null }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mContext = recyclerView.context
    }

    /**
     * 加载图片方法，用于属性扩展
     * @param imageView 图片
     * @param url  数据
     */
    protected abstract fun loadPic(imageView: ImageView, url: String, data: PictureView.PictureBean<*>)

    /**
     * 浏览图片公共方法，用于属性扩展
     */
    protected abstract fun showPic(imageView: ImageView, url: String, data: PictureView.PictureBean<*>)

    /**
     * 获取图片地址
     */
    protected abstract fun getUrl(data: PictureView.PictureBean<*>): String

    /**
     * 点击添加时的公共方法
     */
    protected abstract fun onClickAdd()

    @SuppressLint("NotifyDataSetChanged")
    fun init(selectStyle: Int, customParam1: Int, customParam2: Int, customParam3: Int, maxCount: Int, isPreview: Boolean) {
        mSelectStyle = selectStyle
        mMaxCount = maxCount
        mCustomParam1 = customParam1;
        mCustomParam2 = customParam2;
        mCustomParam3 = customParam3;
        mIsPreview = isPreview;
        insertAddPic()
        notifyDataSetChanged()
    }

    /**
     * @param data MutableList<PicBean<*>>
     */
    @SuppressLint("NotifyDataSetChanged")
    fun initPicList(data: MutableList<PictureView.PictureBean<*>>) {
        mDataList = data
        if (!mIsPreview) insertAddPic()
        notifyDataSetChanged()
    }

    /**
     * 删除图片方法
     * @param position 图片位置索引
     */
    @SuppressLint("NotifyDataSetChanged")
    open fun deletePic(position: Int) {
        mDataList.removeAt(position)
        insertAddPic()
        notifyDataSetChanged()
    }

    /**
     * 更新状态
     * @param isPreview Boolean
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updatePreviewState(isPreview: Boolean) {
        mIsPreview = isPreview
        if (!mIsPreview) insertAddPic() else deleteAddPic()
        notifyDataSetChanged()
    }

    /**
     * 添加加号图片
     */
    open fun addPic(pictureBean: PictureView.PictureBean<*>) {
        deleteAddPic()
        if (getSize() < mMaxCount) {
            mDataList.add(mBlock(pictureBean) ?: pictureBean)
            mDataList.add(pictureBean)
        }
        insertAddPic()
        notifyDataSetChanged()
    }

    /**
     * 外部监听，用于刷新界面及拦截器作用
     */
    fun setAddListener(block: (pictureBean: PictureView.PictureBean<*>) -> PictureView.PictureBean<*>) {
        this.mBlock = block
    }

    /***
     * 获取新增图片
     * @return PictureBean<*>?
     */
    private fun getAddPic() = mDataList.find { it.isAddImg() }

    /**
     * 插入点击增加的占位
     */
    private fun insertAddPic() {
        if (getAddPic() == null) {
            if (getSize() < mMaxCount) {
                val pictureBean = PictureView.PictureBean<String>()
                pictureBean.type = PictureView.PictureBean.TYPE_ADD
                mDataList.add(pictureBean)
            }
        }
    }


    /**
     * 删除点击增加的占位
     */
    private fun deleteAddPic() {
        val addPic = getAddPic()
        if (addPic != null) mDataList.remove(addPic)
    }

    /**
     * clear add pic
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clearPic() {
        mDataList.clear()
        if (!mIsPreview) insertAddPic()
        notifyDataSetChanged()
    }

    /**
     * 获取数据数量
     * @return Int
     */
    private fun getSize() = mDataList.size

    /**
     * 获取已选择数据数量
     * @return Int
     */
    fun getPicSize() = mDataList.filter { !it.isAddImg() }.size

    /**
     * 获取已选择数据数量
     * @return Int
     */
    fun getLocalSize() = mDataList.filter { it.type == PictureView.PictureBean.TYPE_LOCAL }.size

    /**
     * 获取网络选择图
     * @return Int
     */
    fun getNetSize() = mDataList.filter { it.type == PictureView.PictureBean.TYPE_NET }.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_picture_item, parent, false)
        return PicViewHolder(view)
    }

    override fun onBindViewHolder(holder: PicViewHolder, position: Int) {
        val data = mDataList[position]
        val url = getUrl(data)
        if (data.isAddImg()) {
            holder.ivPic.setImageResource(R.drawable.ic_add_image)
        } else {
            loadPic(holder.ivPic, url, data)
        }
        // 根据状态隐藏删除按钮：预览模式或新增图标
        holder.ivDelete.visibility = if (mIsPreview || data.isAddImg()) View.GONE else View.VISIBLE
        holder.ivDelete.setOnClickListener { deletePic(position) }
        holder.ivPic.setOnClickListener { if (data.isAddImg()) onClickAdd() else showPic(holder.ivPic, url, data) }
    }

    override fun getItemCount(): Int = mDataList.size

    class PicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPic: ImageView = itemView.findViewById(R.id.iv_pic)
        val ivDelete: ImageView = itemView.findViewById(R.id.iv_delete)
    }

}