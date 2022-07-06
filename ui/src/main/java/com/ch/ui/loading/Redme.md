# Loading

## 1、更新日志

1. 2022年3月26日15:04:49
   * 重构：使用显示和隐藏控制缺省界面，避免引入其他类

## 2、使用说明

### 2.1、全局初始化

用于全局初始化Loading框架，可在任意界面展示缺省页

```java
LoadingLayout.init()
```

### 2.2、创建缺省页

继承`LoadView`实现创建view，创建View可以传入对应的view或者布局

```java
//获取布局ID
fun getLayoutRes(): Int

//View创建成功回调
fun onViewCreate(view: View)
```

示例如下：

```java
public class EmptyView extends LoadView {

    private  SignleLisenter singleListener;

    public EmptyView(SignleLisenter singleListener){
        this.singleListener=singleListener;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_empty;
    }

    @Override
    public void onViewCreate(@NotNull View view) {
        super.onViewCreate(view);
        view.findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReload();
                singleListener.custom();
            }
        });
    }


    interface SignleLisenter extends ReloadListener{
        void custom();
    }
}
```

* `isEnableReload`：是否开启点击缺省页触发刷新的操作

### 2.3、绑定布局

在我们需要展示的页面初始化`LoadingLayout`框架，初始化时可以传入`View`或`Activity`。

```kotlin
 val loading = LoadingLayout(findViewById<RecyclerView>(R.id.rv), listener = object : ReloadListener {
                override fun onReload(view: View) {
                    Toast.makeText(this@MainActivity, "", Toast.LENGTH_SHORT).show()
                }
            })
```

若我们的布局有特殊需求，需要展示自定义的缺省页或回调，可以使用下面的方法，其他flag用于显示自定义布局时作为区分。

```kotlin
loading.addEmptyView(flag: String, loadView: LoadView)
```

若需要回调，我们可自行在构造函数中进行传递。

### 2.4、显示缺省页

| 方法            | 描述                           |
| --------------- | ------------------------------ |
| `showContent()` | 显示内容布局                   |
| `showLoading()` | 显示加载中的布局               |
| `showError()`   | 显示错误的布局                 |
| `showNetError()`   | 显示网络错误布局 |
| `showEmpty()`   | 显示数据为空的布局             |
| `showOther()`   | 显示其他的布局，主要用于自定义 |

对于自定义的布局，调用` showView(flag: String)`,传入我们的`flag`即可