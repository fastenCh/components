package com.fastenCh.keep.battery;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

public class BatteryUtils {

    /**
     * 打开电池优化界面
     * @param context 上下文
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void openBattery(Context context) {
        @SuppressLint("BatteryLife")
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, 0);
        if (resolveInfo != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 判断是否打开电池优化
     * @param context 上下文
     * @return 是否打开电池优化
     */
    public static boolean isOpenBattery(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (pm == null) {
                return true;
            } else {
               return pm.isIgnoringBatteryOptimizations(context.getPackageName());
            }
        }else {
            return true;
        }
    }
}
