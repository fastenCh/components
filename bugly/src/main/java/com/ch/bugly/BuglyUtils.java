package com.ch.bugly;

import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

public class BuglyUtils {

    private BuglyUtils() {
    }

    private volatile static BuglyUtils instance;

    public static BuglyUtils getInstance() {
        if (instance == null) {
            synchronized (BuglyUtils.class) {
                if (instance == null) {
                    instance = new BuglyUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化方法
     */
    public void init(Context context, String appId, Boolean isDebug) {
        Beta.autoInit = true;
        //升级检查周期设置,默认检查周期为0s
        Beta.upgradeCheckPeriod = 0;
        //设置延迟初始化时间，避免强制升级时造成升级无响应,默认延时3s
        Beta.initDelay = 0;
        //设置开启显示打断策略
        Beta.showInterruptedStrategy = true;
        //设置是否显示消息通知
        Beta.enableNotification = true;
        //热更新能力
        Beta.enableHotfix = false;
        Bugly.init(context, appId, isDebug);
    }

    /**
     * 手动检查升级，初始化成功后方可调用，避免直接调用
     */
    public void checkUpgrade() {
        Beta.checkUpgrade();
    }
}
