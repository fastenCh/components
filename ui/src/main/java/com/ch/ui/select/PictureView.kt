package com.ch.ui.select

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ch.ui.R
import java.lang.NullPointerException

/**
 * 图片选择组件【重构版本】
 * @author ch
 * @date 2022年7月7日11:22:08
 */
class PictureView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attributeSet, defStyleAttr) {
    private var mMaxCount: Int = 6 //最大数量
    private var mSelectStyle: Int = 1 //选择方式，默认为相册
    private var mIsPriView: Boolean

    /** 自定义参数，用于扩展 */
    var mCustomParam1: Int
    var mCustomParam2: Int
    var mCustomParam3: Int

    companion object {
        @JvmField
        var globalViewsAdapter: PictureAdapter<*>? = null
    }

    init {
        overScrollMode = OVER_SCROLL_NEVER
        val a = getContext().obtainStyledAttributes(attributeSet, R.styleable.PicView)
        val spanCount = a.getInt(R.styleable.PicView_spanCount, 3)
        mSelectStyle = a.getInt(R.styleable.PicView_selectStyle, mSelectStyle)
        mCustomParam1 = a.getInt(R.styleable.PicView_customParam1, 1)
        mCustomParam2 = a.getInt(R.styleable.PicView_customParam2, 1)
        mCustomParam3 = a.getInt(R.styleable.PicView_customParam3, 1)
        mIsPriView = a.getBoolean(R.styleable.PicView_isPriView, false)
        mMaxCount = a.getInt(R.styleable.PicView_maxCount, mMaxCount)
        a.recycle()
        layoutManager = GridLayoutManager(context, spanCount)
        if (globalViewsAdapter != null) {
            setAdapter(globalViewsAdapter!!)
        }
    }

    /** 设置Adapter */
    fun setAdapter(adapter: PictureAdapter<*>) {
        super.setAdapter(adapter)
        adapter.init(mSelectStyle, mCustomParam1, mCustomParam2, mCustomParam3, mMaxCount, mIsPriView)
    }

    private fun getPicAdapter(): PictureAdapter<*>? {
        return adapter as PictureAdapter<*>
    }

    @Deprecated("setAdapter(adapter: PictureAdapter<*>)")
    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
    }

    /**
     * 更新状态
     * @param isPreview Boolean
     */
    fun setIsPreview(isPreview: Boolean) = getPicAdapter()?.updatePreviewState(isPreview)

    /**
     * 获取所有图片数据，此处不包含需要添加的图标
     * @return List<PicBean<*>> 图片集合
     */
    fun getPicList(): List<PictureBean<*>> {
        if (getPicAdapter() == null) {
            throw NullPointerException("please call method setAdapter() or set global adapter")
        }
        return getPicAdapter()!!.mDataList.filter { it.type != PictureBean.TYPE_ADD }
    }

    /**
     *
     * @param picType
     * @return List<PicBean<*>> 图片集合
     */
    fun <M> getPicListByType(picType: Int): List<M> {
        if (getPicAdapter() == null) {
            throw NullPointerException("please call method setAdapter() or set global adapter")
        }
        val filter = getPicAdapter()!!.mDataList.filter { it.type == picType }
        val data = mutableListOf<M>();
        filter.forEach { data.add(it.data as M) }
        return data
    }

    /**
     * 获取所有本地图片数据，此处不包含需要添加的图标
     * @return List<PicBean<*>> 图片集合
     */
    fun <M> getPicLocalList(): List<M> {
        if (getPicAdapter() == null) {
            throw NullPointerException("please call method setAdapter() or set global adapter")
        }
        return getPicListByType(PictureBean.TYPE_LOCAL);
    }

    /**
     * 针对已经有数据的情况，提供初始化数据的方法
     * @param data List<PicBean<*>>
     */
    fun initPicList(data: MutableList<PictureBean<*>>) {
        if (getPicAdapter() == null) {
            throw NullPointerException("please call method setAdapter() or set global adapter")
        }
        getPicAdapter()?.initPicList(data)
    }

    /**
     * 针对已经有数据的情况，提供初始化数据的方法
     * @param data List<PicBean<*>>
     */
    fun <M> initPicList(data: MutableList<M>, picType: Int) {
        if (getPicAdapter() == null) {
            throw NullPointerException("please call method setAdapter() or set global adapter")
        }
        val list = mutableListOf<PictureBean<M>>()
        data.forEach {
            val bean = PictureBean<M>()
            bean.data = it
            bean.type = picType
            list.add(bean)
        }
        initPicList(list as MutableList<PictureBean<*>>)
    }

    /**
     * 获取所有网络图片数据，此处不包含需要添加的图标
     * @return List<PicBean<*>> 图片集合
     */
    fun <M> getNetList(): List<M> {
        if (getPicAdapter() == null) {
            throw NullPointerException("please call method setAdapter() or set global adapter")
        }
        return getPicListByType(PictureBean.TYPE_NET);
    }

    /**
     * 外部监听，用于刷新界面及拦截器作用
     */
    fun setAddListener(block: (pictureBean: PictureBean<*>) -> PictureBean<*>) {
        getPicAdapter()?.setAddListener(block)
    }

    /**
     * clear add pic
     */
    fun clearPic() {
        getPicAdapter()?.clearPic()
    }

    class PictureBean<T> {
        companion object {
            const val TYPE_ADD = 0
            const val TYPE_LOCAL = 1
            const val TYPE_NET = 2
            const val TYPE_OTHER = 3
        }

        var data: T? = null
        var type: Int = TYPE_LOCAL

        fun isAddImg() = type == TYPE_ADD
    }
}