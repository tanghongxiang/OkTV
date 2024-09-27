package com.thx.resourcelib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Field


/**
 * @Description:APP辅助工具类
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @Create Date: 2022/6/28 18:29
 */

/**
 * 跳转到应用设置页面-应用列表
 */
fun jumpToPermissionsSetting(mContext: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.putExtra("packageName", mContext.packageName)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        mContext.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 跳转到应用设置页面
 */
fun jumpToAppSettingPage(mContext: Context) {
    val intent = Intent()
    intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.putExtra("packageName", mContext.packageName)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.data = Uri.fromParts("package", mContext.packageName, null)
    try {
        mContext.startActivity(intent)
    } catch (e: Exception) {
        jumpToPermissionsSetting(mContext)
    }
}

fun installApp(file: File, context: Context) {
    val apkUri = FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName + ".fileprovider",
        file
    )
    //在AndroidManifest中的android:authorities值
    val install = Intent(Intent.ACTION_VIEW)
    install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    install.setDataAndType(apkUri, "application/vnd.android.package-archive")
    context.startActivity(install)
}

/**
 * 复制内容到剪切板
 *
 * @param copyStr
 * @return
 */
fun copyToClipBoard(context: Context, copyStr: String): Boolean {
    return try {
        //获取剪切板管理器
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //设置内容到剪切板
        cm.setPrimaryClip(ClipData.newPlainText(null, copyStr))
        true
    } catch (e: java.lang.Exception) {
        false
    }
}

/**
 * 读取剪切板内容
 */
fun readClipBoardContent(context: Context): String? {
    val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        ?: return null
    if (manager.hasPrimaryClip() && (manager.primaryClip?.itemCount ?: 0) > 0) {
        return manager.primaryClip?.getItemAt(0)?.text?.toString()
    }
    return null
}


/**
 * 清除剪切板内容
 */
fun clearClipBoardContent(context: Context): String? {
    copyToClipBoard(context, "")
    return null
}


private fun checkNotifySetting(context: Context): Boolean {
    val manager = NotificationManagerCompat.from(context)
    return manager.areNotificationsEnabled()
}

/**
 * 读取手机SIM卡号
 */
@SuppressLint("HardwareIds", "MissingPermission")
fun initIsDoubleTelephone(context: Context): String {
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    var phoneNum = ""
    try {
        phoneNum = tm.line1Number.replace("+86", "")
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return phoneNum
}

/**
 * 隐藏软键盘
 */
fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
    if (imm == null || window.attributes.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) return
    if (currentFocus == null) return
    imm.hideSoftInputFromWindow(
        currentFocus?.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

/**
 * 当前应用是否处于前台
 */
private fun isForeground(context: Context?): Boolean {
    if (context == null) return false
    val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
    val processes = activityManager.runningAppProcesses ?: return false
    for (processInfo in processes) {
        if (processInfo.processName != context.packageName) continue
        if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            //这个处于前台的进程是否是我们的进程
            return processInfo.processName == "com.art.amber.collector"
        }
    }
    return false
}

/**
 * 获取藏宝阁本地文件保存路径
 */
fun getSaveLocalPath(context: Context) =
    Environment.getExternalStorageDirectory().absolutePath + "/DCIM/amber/"
//    context.getExternalFilesDir(null)?.absolutePath?.toString() + "/amber/download/video/"

/**
 * 获取分享图片临时目录
 */
fun getShareTempFilePath(context: Context): String {
    val path = context.getExternalFilesDir(null)?.absolutePath?.toString() + "/share/img/"
    val file = File(path)
    if (file.mkdirs()) {
        return path
    }
    return path
}

fun writeBitmapToFile(bitmap: Bitmap, dst: String?, quality: Int) {
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos)
    bitmap.recycle()
    val fos = FileOutputStream(dst)
    fos.write(bos.toByteArray())
    fos.flush()
    fos.close()
    bos.close()
}

/**
 * 获取打开此应用的APP名称
 */
fun Activity.getStartAppNameFromOpenMine(): String? {
    var startPackageName: String? = null
    try {
        val activityClass = Class.forName("android.app.Activity")
        val refererField: Field = activityClass.getDeclaredField("mReferrer")
        refererField.isAccessible = true
        startPackageName = refererField.get(this)?.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return getAppNameFromPackageName(startPackageName)
}

/**
 * 根据报名确定APP的名称
 */
private fun getAppNameFromPackageName(name: String?): String? = when (name) {
    "tv.danmaku.bili" -> "哔哩哔哩"
    else -> name
}

/**
 * 用默认浏览器打开URL
 */
fun Activity.openUrlInDefaultBrowser(mUrl: String?) {
    try {
        var jumpUrl = mUrl ?: return
        if (!jumpUrl.startsWith("http")) {
            jumpUrl = "http://$jumpUrl"
        }
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val url = Uri.parse(jumpUrl)
        intent.data = url
        intent.setDataAndType(url, "text/html")
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
