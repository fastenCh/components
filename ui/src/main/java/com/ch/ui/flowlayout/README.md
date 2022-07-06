# FlowLayout

[来自弘扬FlowLayout，已对源码进行修改](https://hub.fastgit.org/hongyangAndroid/FlowLayout)

Android流式布局，支持单选、多选等，适合用于产品标签等。

## 用法

```groovy
// 原始：implementation 'com.hyman:flowlayout-lib:1.1.2'
// 修改后的库
implementation 'com.vvise.architecture:lib_ui:1.x.x'
```

### 声明

布局文件中声明：

```xml
 <com.fastench.ui.flowlayout.TagFlowLayout
        android:id="@+id/id_flowlayout"
        app:max_select="-1"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:padding="20dp">
    </com.fastench.ui.flowlayout.TagFlowLayout>
```

支持属性：

`max_select`：`-1`为不限制选择数量，`>=1`的数字为控制选择tag的数量

支持通过`state = checked`来控制选中和取消,也可以自己在`Adapter `的`onSelected`和`unSelected`中分别处理显示。

### 设置数据

```java
mFlowLayout.setAdapter(new TagAdapter<String>(mVals) {
    @Override
    public View getView(FlowLayout parent, int position, String s) {
        TextView tv = (TextView) mInflater.inflate(R.layout.tv,mFlowLayout, false);
        tv.setText(s);
        return tv;
    }
});
```

getView中回调，类似ListView等用法。

### 对于选中状态

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:color="@color/tag_select_textcolor"
          android:drawable="@drawable/checked_bg"
          android:state_checked="true"></item>
    <item android:drawable="@drawable/normal_bg"></item>
</selector>
```

也可以不依赖`state_checked`,在下面的回调中自行设置:

```java
#Adapter
    @Override
    public void onSelected(int position, View view) {
        super.onSelected(position, view);
    }

    @Override
    public void unSelected(int position, View view) {
        super.unSelected(position, view);
    }
```

### 事件

```java
mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        Toast.makeText(getActivity(), mVals[position], Toast.LENGTH_SHORT).show();
        return true;
    }
});
```

点击标签时的回调。

```java
mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
    @Override
    public void onSelected(Set<Integer> selectPosSet) {
        getActivity().setTitle("choose:" + selectPosSet.toString());
    }
});
```

选择多个标签时的回调。

## 预先设置`Item`选中

```java
// 预先设置选中
mAdapter.setSelectedList(1,3,5,7,8,9);
// 获得所有选中的pos集合
flowLayout.getSelectedList();
//刷新时会清除所有选中
flowLayout.onChanged()
 //设置取消选中每个Item
flowLayout.setUnSelect(0)
```

