package com.ch.ui.address

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ch.ui.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

/**
 *
 * 重构地址选择对话框，使用BottomSheetDialog实现
 *
 * @author ch
 * @date 2022年7月11日09:26:47
 *
 * @property initIds 选择列表
 * @property getProvince 获取省数据
 * @property getCity 获取市数据
 * @property getArea 获取区数据
 * @property value Map<AddressMode, String>
 * @property addressMode 选择模式
 * @property selectScopeMode 选择范围
 * @property onSuccess 成功回调
 */
class AddressPopup(
    context: Context,
    private val initIds: Array<String> = arrayOf("", "", ""),
    private val getProvince: suspend () -> MutableList<Address>,
    private val getCity: suspend (provinceId: String) -> MutableList<Address>,
    private val getArea: suspend (cityId: String) -> MutableList<Address>,
    private val addressMode: Set<AddressMode> = setOf(AddressMode.P, AddressMode.C, AddressMode.A),
    private val selectScopeMode: Set<AddressMode> = setOf(),
    private val onSuccess: (provinceAddress: Address?, cityAddress: Address?, areaAddress: Address?) -> Unit
) : BottomSheetDialog(context, R.style.DialogTheme) {

    private lateinit var mAdapterList: Array<AddressAdapter>
    private val mRvList: MutableList<RecyclerView> by lazy {
        mutableListOf(
            findViewById<RecyclerView>(R.id.rv_province) as RecyclerView,
            findViewById<RecyclerView>(R.id.rv_city) as RecyclerView,
            findViewById<RecyclerView>(R.id.rv_area) as RecyclerView
        )
    }
    private val mIvClose: ImageView by lazy { findViewById<ImageView>(R.id.iv_close) as ImageView }
    private val tvFinish: TextView by lazy { findViewById<TextView>(R.id.tv_finish) as TextView }
    private val tLSelect: TabLayout by lazy { findViewById<TabLayout>(R.id.tl_menu) as TabLayout }
    private val tvMsg: TextView by lazy { findViewById<TextView>(R.id.tv_msg) as TextView }
    private val progress: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progress) as ProgressBar }
    private val loading: LinearLayoutCompat by lazy { findViewById<LinearLayoutCompat>(R.id.ll_loading) as LinearLayoutCompat }
    private var selectPos = arrayOf(-1, -1, -1)

    //错误请求索引，用于重新请求
    private var errorGetIndex = -1;

    init {
        if (initIds.size != 3) {
            throw Exception("AddressPopup initIds params must size is 3")
        }
        setContentView(R.layout.popup_address)
    }

    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window = window ?: return
        // 沉浸式状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // 隐藏底部导航栏
        val decorView: View = window.decorView
        val uiOptions: Int = (decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = uiOptions
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        mAdapterList = arrayOf(AddressAdapter(context), AddressAdapter(context), AddressAdapter(context))

        loading.setOnClickListener { reload() }
        mIvClose.setOnClickListener { dismiss() }

        //用于非确定性选择，如可以选择省点击确定、选择市点击确定
        tvFinish.setOnClickListener {
            var pAddress: Address? = null
            if (selectPos[0] != -1) {
                pAddress = mAdapterList[0].getItem(selectPos[0])
            }
            var cityAddress: Address? = null
            if (selectPos[1] != -1) {
                cityAddress = mAdapterList[1].getItem(selectPos[1])
            }
            var areaAddress: Address? = null
            if (selectPos[2] != -1) {
                areaAddress = mAdapterList[2].getItem(selectPos[2])
            }
            onSuccess(pAddress, cityAddress, areaAddress)
            dismiss()
        }
        tLSelect.addTab(tLSelect.newTab().setText("请选择"))
        tLSelect.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                //排除点击最后一个菜单
                if (tLSelect.tabCount != position + 1) {
                    mRvList[tLSelect.tabCount - 1].visibility = View.GONE
                    mRvList[position].visibility = View.VISIBLE
                    hideLoading()
                    //注意移除顺序
                    if (tLSelect.getTabAt(position + 2) != null) {
                        tLSelect.removeTabAt(position + 2)
                    }
                    if (tLSelect.getTabAt(position + 1) != null) {
                        tLSelect.removeTabAt(position + 1)
                    }
                }
                if (position == 0) {
                    //移除选择的市区
                    selectPos[1] = -1
                    selectPos[2] = -1
                } else if (position == 1) {
                    //移除选择的区
                    selectPos[2] = -1
                }

                tvFinish.visibility = if (
                    (position == 0 && selectScopeMode.contains(AddressMode.P)) ||
                    (position == 1 && selectScopeMode.contains(AddressMode.C)) ||
                    (position == 2 && selectScopeMode.contains(AddressMode.A))
                ) View.VISIBLE else View.GONE
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        mRvList.forEachIndexed { index, recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(context)
            mAdapterList[index].clickItem = { position ->
                onItemClick(index, position)
            }
            recyclerView.adapter = mAdapterList[index]
        }
        if (addressMode.contains(AddressMode.P)) {
            getProvinceData()
        } else if (addressMode.size == 1 && addressMode.contains(AddressMode.C)) {
            if (initIds.isEmpty()) {
                Toast.makeText(context, "请输入省数据", Toast.LENGTH_SHORT).show()
            }
            getCityData(initIds[0])
            mRvList[0].visibility = View.GONE
            mRvList[1].visibility = View.VISIBLE
        } else if (addressMode.size == 1 && addressMode.contains(AddressMode.A)) {
            if (initIds.isEmpty()) {
                Toast.makeText(context, "请输入市数据", Toast.LENGTH_SHORT).show()
            }
            getAreaData(initIds[1])
            mRvList[0].visibility = View.GONE
            mRvList[2].visibility = View.VISIBLE
        }
        selectDefault(initIds)
    }

    override fun show() {
        super.show()
        reload()
    }

    /**
     * 重新加载
     */
    private fun reload() {
        when (errorGetIndex) {
            0 -> getProvinceData()
            1 -> getCityData(initIds[1]) //传入省id
            2 -> getAreaData(initIds[2])//传入市id
        }
    }

    private fun onItemClick(index: Int, position: Int, isDefaultSelect: Boolean = false) {
        tvFinish.visibility = if (
            (index == 0 && selectScopeMode.contains(AddressMode.C)) ||
            ((index == 1 || index == 2) && selectScopeMode.contains(AddressMode.A))
        ) View.VISIBLE else View.GONE

        val item = mAdapterList[index].getItem(position)
        initIds[index] = item.TEXT
        //设置选中颜色
        item.isChosen = true
        mAdapterList[index].notifyItemChanged(position)
        //恢复之前选择的颜色
        if (selectPos[index] != -1 && position != selectPos[index] && selectPos[index] < mAdapterList[index].itemCount) {
            mAdapterList[index].getItem(selectPos[index]).isChosen = false
            mAdapterList[index].notifyItemChanged(selectPos[index])
        }
        selectPos[index] = position

        //点击省时判断是否允许选择市
        if (index == 0 && !addressMode.contains(AddressMode.C)) {
            if (!isDefaultSelect) {
                onSuccess(mAdapterList[index].getItem(position), null, null)
                dismiss()
                return
            }
        }
        //点击市时判断是否允许选择区
        if (index == 1 && !addressMode.contains(AddressMode.A)) {
            if (!isDefaultSelect) {
                var pAddress: Address? = null
                if (selectPos[0] != -1) {
                    pAddress = mAdapterList[0].getItem(selectPos[0])
                }
                onSuccess(pAddress, mAdapterList[index].getItem(position), null)
                dismiss()
                return
            }
        }
        //若可选市地区
        if (index == 1 && selectScopeMode.contains(AddressMode.C)) tvFinish.visibility = View.VISIBLE

        //设置TabLayout
        tLSelect.getTabAt(index)?.text = item.TEXT
        if (index < mRvList.size - 1) {
            if (tLSelect.getTabAt(index + 1) == null) {
                val newTab = tLSelect.newTab().setText("请选择")
                tLSelect.addTab(newTab)
                newTab.select()
            }
        }
        when (index) {
            //点击省即获取市数据
            0 -> {
                getCityData(item.ID)
                selectPos[1] = -1
            }
            //点击市即获取区数据
            1 -> {
                getAreaData(item.ID)
                selectPos[2] = -1
            }
            //点击区数据
            2 -> {
                if (!isDefaultSelect) {
                    var pAddress: Address? = null
                    if (selectPos[0] != -1) {
                        pAddress = mAdapterList[0].getItem(selectPos[0])
                    }
                    var cityAddress: Address? = null
                    if (selectPos[1] != -1) {
                        cityAddress = mAdapterList[1].getItem(selectPos[1])
                    }
                    onSuccess(pAddress, cityAddress, mAdapterList[index].getItem(position))
                    dismiss()
                }
            }
        }
        if (index < mRvList.size - 1) {
            mRvList[index].visibility = View.GONE
            mRvList[index + 1].visibility = View.VISIBLE
        }
    }

    /**
     * 获取省数据
     */
    private fun getProvinceData() {
        showLoading()
        GlobalScope.launch {
            kotlin.runCatching {
                getProvince()
            }.onSuccess {
                errorGetIndex = -1
                withContext(Dispatchers.Main) {
                    mAdapterList[0].setNewInstance(it)
                    mAdapterList[0].notifyDataSetChanged()
                    hideLoading()
                }
            }.onFailure {
                errorGetIndex = 0
                withContext(Dispatchers.Main) {
                    showError()
                }
            }
        }
    }

    /**
     * 获取市数据
     */
    private fun getCityData(provinceId: String) {
        showLoading()
        GlobalScope.launch {
            kotlin.runCatching {
                getCity(provinceId)
            }.onSuccess {
                errorGetIndex = -1
                withContext(Dispatchers.Main) {
                    mAdapterList[1].setNewInstance(it)
                    mAdapterList[1].notifyDataSetChanged()
                    hideLoading()
                }
            }.onFailure {
                errorGetIndex = 1
                withContext(Dispatchers.Main) {
                    showError()
                }
            }
        }
    }


    /**
     * 获取区数据
     */
    private fun getAreaData(cityId: String) {
        showLoading()
        GlobalScope.launch {
            kotlin.runCatching {
                getArea(cityId)
            }.onSuccess {
                errorGetIndex = -1
                withContext(Dispatchers.Main) {
                    mAdapterList[2].setNewInstance(it)
                    mAdapterList[2].notifyDataSetChanged()
                    hideLoading()
                }
            }.onFailure {
                errorGetIndex = 2
                withContext(Dispatchers.Main) {
                    showError()
                }
            }
        }
    }

    /**
     * @param ids 省ID、市ID、区ID
     */
    fun selectDefault(ids: Array<String>) {
        //省数据已经初始化，无需获取，模拟点击省、市、区
        ids.forEachIndexed { index, id ->
            if (id.isNotEmpty()) {
                mAdapterList[index].data.forEachIndexed { pos, address ->
                    if (address.ID == id) {
                        onItemClick(index, pos, true)
                    }
                }
            }
        }
    }

    /**
     * 显示loading
     */
    private fun showLoading() {
        progress.visibility = View.VISIBLE
        loading.visibility = View.VISIBLE
        tvMsg.text = "正在加载中..."
    }

    /**
     * 隐藏loading
     */
    private fun hideLoading() {
        loading.visibility = View.GONE
    }

    /**
     * 隐藏loading
     */
    private fun showError() {
        progress.visibility = View.GONE
        tvMsg.text = "请求失败，点击重试..."
    }


    /**
     * 地址适配器
     */
    class AddressAdapter(context: Context) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
        private val colorPrimary: Int
        private val normalColor: Int = Color.parseColor("#33333")
        var data: List<Address> = ArrayList()
        var clickItem: ((position: Int) -> Unit)? = null

        init {
            val array: TypedArray = context.theme.obtainStyledAttributes(intArrayOf(androidx.appcompat.R.attr.colorPrimary))
            colorPrimary = array.getColor(0, Color.TRANSPARENT)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.popup_address_item, parent, false)
            return AddressViewHolder(view)
        }

        override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
            val address = data[position]
            holder.tvName.text = address.TEXT
            holder.tvName.setTextColor(if (address.isChosen) colorPrimary else normalColor)
            holder.tvName.setOnClickListener { clickItem?.invoke(position) }
        }

        override fun getItemCount() = data.size

        fun getItem(position: Int) = data[position]

        fun setNewInstance(data: List<Address>) {
            this.data = data
        }

        class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvName: TextView = itemView.findViewById(R.id.tv_name)
        }
    }


    @Keep
    inner class Address(
        val TEXT: String,
        val ID: String,
        var isChosen: Boolean = false
    )

    enum class AddressMode {
        P, C, A
    }
}