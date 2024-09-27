package com.thx.resourcelib.ext

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.thx.resourcelib.utils.currentCountry
import com.thx.resourcelib.utils.currentLanguage
import com.thx.resourcelib.utils.getSysPreferredLocale
import com.thx.resourcelib.utils.getSystemLanguage
import java.util.Locale


/**
 * 获取字符串资源
 */
fun getResourceString(context: Context, resId: Int): String {
//    return try {
//        ContextCompat.getString(context.applicationContext, resId)
//    } catch (e: Exception) {
//        ""
//    }
    val resources = getApplicationResource(
        context.applicationContext.packageManager,
        context.applicationContext.packageName,
        Locale(currentLanguage(), currentCountry())
    )
    return if (resources == null) {
        getCommonResourceString(context,resId)
    } else {
        try {
            resources.getString(resId)
        } catch ( e:Exception) {
            getCommonResourceString(context,resId)
        }
    }

}

/**
 * 启动页多语言获取(mmkv&sp未初始化的时候)根据手机系统语言适配
 */
fun getAppDefaultResourceString(context: Context, resId: Int): String {
    val systemLanguageInfo = getSysPreferredLocale()
    var language = systemLanguageInfo.language?:"en"
    var country = systemLanguageInfo.country?:"US"
    // 不是中文，就显示英文
    if("zh"!=language){
        language = "en"
        country = "US"
    }
    val resources = getApplicationResource(
        context.applicationContext.packageManager,
        context.applicationContext.packageName,
        Locale(language, country)
    )
    return if (resources == null) {
        getCommonResourceString(context,resId)
    } else {
        try {
            resources.getString(resId)
        } catch ( e:Exception) {
            getCommonResourceString(context,resId)
        }
    }

}

private fun getCommonResourceString(context:Context,resId:Int):String{
    return try {
        ContextCompat.getString(context.applicationContext, resId)
    } catch (e: Exception) {
        ""
    }
}

private fun getApplicationResource(pm: PackageManager, pkgName: String, l: Locale): Resources? {
    var resourceForApplication: Resources? = null
    try {
        resourceForApplication = pm.getResourcesForApplication(pkgName)
        updateResource(resourceForApplication, l)
    } catch (_: Exception) {

    }
    return resourceForApplication
}

private fun updateResource(resource: Resources, l: Locale) {
    val config = resource.configuration
    config.locale = l
    resource.updateConfiguration(config, null)
}

/**
 * 启动页多语言获取(mmkv&sp未初始化的时候)根据手机系统语言适配
 */
fun getAppDefaultResourceImg(context: Context, resId: Int): Drawable? {
    val systemLanguageInfo = getSysPreferredLocale()
    var language = systemLanguageInfo.language?:"en"
    var country = systemLanguageInfo.country?:"US"
    // 不是中文，就显示英文
    if("zh"!=language){
        language = "en"
        country = "US"
    }
    val resources = getApplicationResource(
        context.applicationContext.packageManager,
        context.applicationContext.packageName,
        Locale(language, country)
    )
    return if (resources == null) {
        getCommonResourceDrawable(context,resId)
    } else {
        try {
            resources.getDrawable(resId,null)
        } catch ( e:Exception) {
            getCommonResourceDrawable(context,resId)
        }
    }
}


/**
 * 获取图片资源
 */
fun getResourceImg(context: Context, resId: Int): Drawable? {
    val resources = getApplicationResource(
        context.applicationContext.packageManager,
        context.applicationContext.packageName,
        Locale(currentLanguage(), currentCountry())
    )
    return if (resources == null) {
        getCommonResourceDrawable(context,resId)
    } else {
        try {
            resources.getDrawable(resId,null)
        } catch ( e:Exception) {
            getCommonResourceDrawable(context,resId)
        }
    }
}

/**
 * 直接获取图片资源
 */
private fun getCommonResourceDrawable(context:Context,resId:Int):Drawable?{
    return try {
        ContextCompat.getDrawable(context.applicationContext, resId)
    } catch (e: Exception) {
        null
    }
}