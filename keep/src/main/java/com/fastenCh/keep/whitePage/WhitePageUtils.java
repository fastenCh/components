package com.fastenCh.keep.whitePage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

public class WhitePageUtils {

    public void openWhitePage(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName componentName = null;
        if (RomUtils.isXiaomi()) {
            intent.setAction("miui.intent.action.OP_AUTO_START");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
        } else if (RomUtils.isSmartisan()) {
            intent.setAction(Settings.ACTION_SETTINGS);
        } else if (RomUtils.isLeTv()) {
            intent.setAction("com.letv.android.permissionautoboot");
        } else if (RomUtils.isSamsung()) {
            componentName = getSamSungComponentName();
        } else if (RomUtils.isOneplus()) {
            componentName = getOnePlusComponentName();
        } else if (RomUtils.isVivo()) {
            componentName = getViVoComponentName();
        } else if (RomUtils.isOppo()) {
            componentName = getOppoComponentName();
        } else if (RomUtils.is360()) {
            componentName = get360ComponentName();
        } else if (RomUtils.isMeizu()) {
            componentName = getFlyMeComponentName();
        } else if (RomUtils.isHuawei()) {
            componentName = getHuaweiComponentName();
        } else if (RomUtils.isHarmonyOSa()) {
            componentName = getHarmonyOSComponentName();
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
        }
        if (null != componentName || null != intent.getAction()) {
            if (componentName != null) {
                intent.setComponent(componentName);
            }
            context.startActivity(intent);
        }
    }


    private ComponentName getHuaweiComponentName() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } else {
            return new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
        }
    }

    private ComponentName getHarmonyOSComponentName() {
        return new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.mainscreen.MainScreenActivity");
    }

    private ComponentName getSamSungComponentName() {
        return new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity");
    }

    private ComponentName getViVoComponentName() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity");
        } else {
            return new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.SoftwareManagerActivity");
        }
    }

    private ComponentName getOppoComponentName() {
        return new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity");
    }

    private ComponentName getOnePlusComponentName() {
        return new ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity");
    }

    private ComponentName get360ComponentName() {
        return new ComponentName("com.yulong.android.coolsafe", "com.yulong.android.coolsafe.ui.activity.autorun.AutoRunListActivity");
    }

    private ComponentName getFlyMeComponentName() {
        return ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");
    }
}
