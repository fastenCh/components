package com.ch.jpush

import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.service.JPushMessageReceiver
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.CmdMessage
import cn.jpush.android.api.JPushMessage
import com.ch.jpush.TagAliasOperatorHelper
import org.json.JSONException
import org.json.JSONObject

val pushInterceptor: ((String, String, String) -> Unit)? = null

class PushMessageReceiver : JPushMessageReceiver() {

    private val TAG = "PushMessageReceiver"


    override fun onMessage(context: Context, customMessage: CustomMessage) {
        Log.e(Companion.TAG, "[onMessage] $customMessage")
        val intent = Intent("com.jiguang.demo.message")
        intent.putExtra("msg", customMessage.message)
        context.sendBroadcast(intent)
    }

    override fun onNotifyMessageOpened(context: Context, message: NotificationMessage) {
        try {
            val jsonObject = JSONObject(message.notificationExtras)
            val code = jsonObject["CODE"] as String
            val msg = jsonObject["VAL"] as String
            pushInterceptor?.invoke(code, msg, message.notificationExtras)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onMultiActionClicked(context: Context, intent: Intent) {
        Log.e(Companion.TAG, "[onMultiActionClicked] 用户点击了通知栏按钮")
        val nActionExtra = intent.extras!!.getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA)

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(Companion.TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null")
            return
        }
        if (nActionExtra == "my_extra1") {
            Log.e(Companion.TAG, "[onMultiActionClicked] 用户点击通知栏按钮一")
        } else if (nActionExtra == "my_extra2") {
            Log.e(Companion.TAG, "[onMultiActionClicked] 用户点击通知栏按钮二")
        } else if (nActionExtra == "my_extra3") {
            Log.e(Companion.TAG, "[onMultiActionClicked] 用户点击通知栏按钮三")
        } else {
            Log.e(Companion.TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义")
        }
    }

    override fun onNotifyMessageArrived(context: Context, message: NotificationMessage) {
        Log.e(Companion.TAG, "[onNotifyMessageArrived] $message")
    }

    override fun onNotifyMessageDismiss(context: Context, message: NotificationMessage) {
        Log.e(Companion.TAG, "[onNotifyMessageDismiss] $message")
    }

    override fun onRegister(context: Context, registrationId: String) {
        Log.e(Companion.TAG, "[onRegister] $registrationId")
        val intent = Intent("com.jiguang.demo.register")
        context.sendBroadcast(intent)
    }

    override fun onConnected(context: Context, isConnected: Boolean) {
        Log.e(Companion.TAG, "[onConnected] $isConnected")
    }

    override fun onCommandResult(context: Context, cmdMessage: CmdMessage) {
        Log.e(Companion.TAG, "[onCommandResult] $cmdMessage")
    }

    override fun onTagOperatorResult(context: Context, jPushMessage: JPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage)
        super.onTagOperatorResult(context, jPushMessage)
    }

    override fun onCheckTagOperatorResult(context: Context, jPushMessage: JPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage)
        super.onCheckTagOperatorResult(context, jPushMessage)
    }

    override fun onAliasOperatorResult(context: Context, jPushMessage: JPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage)
        super.onAliasOperatorResult(context, jPushMessage)
    }

    override fun onMobileNumberOperatorResult(context: Context, jPushMessage: JPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage)
        super.onMobileNumberOperatorResult(context, jPushMessage)
    }

    override fun onNotificationSettingsCheck(context: Context, isOn: Boolean, source: Int) {
        super.onNotificationSettingsCheck(context, isOn, source)
        Log.e(Companion.TAG, "[onNotificationSettingsCheck] isOn:$isOn,source:$source")
    }

    companion object {
        private const val TAG = "PushMessageReceiver"
    }
}