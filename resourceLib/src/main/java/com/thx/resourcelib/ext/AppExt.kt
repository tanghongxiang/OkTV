package com.thx.resourcelib.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import com.hjq.toast.Toaster
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.thx.resourcelib.base.BaseApplication

/**
 * 获取APP包信息，包括版本号等
 */
fun Activity.getPackageInfo(): PackageInfo {
    return packageManager.getPackageInfo(packageName, 0)
}

/**
 * 判断是否安装支付宝
 */
fun isAlipayAppInstalledAndSupported(context: Context): Boolean {
    val pm = context.packageManager
    return try {
        val info: PackageInfo = pm.getPackageInfo("com.eg.android.AlipayGphone", 0)
        info != null
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        false
    }
}

/**
 * 打开邮箱
 */
fun openEmailApp(){
    try {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
        // 收件人
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ambermusicpass@gmail.com"))
        // 邮件主题
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
        // 邮件正文内容
        emailIntent.putExtra(Intent.EXTRA_TEXT, arrayOf(""))
        BaseApplication.getInstance().topActivity()?.startActivity(emailIntent)
    }catch (e:Exception){
        Toaster.showLong("邮箱打开失败")
    }
}