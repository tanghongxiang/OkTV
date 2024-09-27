package com.thx.resourcelib.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.text.TextUtils
import android.util.Log
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import com.thx.resourcelib.base.BaseApplication
import java.util.Locale

/**
 * 获取手机系统语言
 */
fun getSystemLanguage(): LocaleListCompat {
    val configuration = Resources.getSystem().configuration
    return ConfigurationCompat.getLocales(configuration)
}

/**
 * 获取应用语言
 */
fun getAppLocale(context: Context): Locale {
    val local: Locale
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        local = context.resources.configuration.locales.get(0)
    } else {
        local = context.resources.configuration.locale
    }
    return local
}

/**
 * 获取系统首选语言
 * 注意：该方法获取的是用户实际设置的不经API调整的系统首选语言
 * @return
 */
fun getSysPreferredLocale(): Locale {
    val locale: Locale
    //7.0以下直接获取系统默认语言
    if (Build.VERSION.SDK_INT < 24) {
        // 等同于context.getResources().getConfiguration().locale
        locale = Locale.getDefault()
        // 7.0以上获取系统首选语言
    } else {
        /*
         * 以下两种方法等价，都是获取经API调整过的系统语言列表（可能与用户实际设置的不同）
         * 1.context.getResources().getConfiguration().getLocales()
         * 2.LocaleList.getAdjustedDefault()
         */
        // 获取用户实际设置的语言列表
        locale = LocaleList.getDefault().get(0)
    }
    return locale
}

/**
 * 判断SharedPrefences中存储和app中的多语言信息是否相同
 */
fun isSameWithSetting(context: Context): Boolean {
    val locale = getAppLocale(context)
    val language = locale.language
    val country = locale.country
    val spLanguage =
        MMKVUtils.mInstance.decodeStringDef(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_LANGUAGE, "")
    val spCountry = MMKVUtils.mInstance.decodeStringDef(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_AREA, "")
    return language.equals(spLanguage) && country.equals(spCountry)
}

/**
 * 保存多语言信息到sp中
 */
fun saveLanguageSetting(context: Context, locale: Locale) {
    MMKVUtils.mInstance.encode(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_LANGUAGE, locale.language)
    MMKVUtils.mInstance.encode(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_AREA, locale.country)
    Log.e(
        "xiang",
        "=====保存的语言：${MMKVUtils.mInstance.decodeString(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_LANGUAGE)}"
    )
    Log.e(
        "xiang",
        "=====保存的国家：${MMKVUtils.mInstance.decodeString(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_AREA)}"
    )
}

/**
 * 获取当前语言
 */
fun getCurrentAppLanguage(): String {
    var appLanguage =
        MMKVUtils.mInstance.decodeStringDef(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_LANGUAGE, "")
    if (appLanguage == null || TextUtils.isEmpty(appLanguage)) {
        appLanguage = getSysPreferredLocale().language ?: "zh"
    }
    return appLanguage
}

/**
 * 更换APP语言
 * @param context:上下文对象
 * @param language:语言
 * @param area:地区
 */
fun changeLanguage(context: Context, language: String, area: String) {
    if (TextUtils.isEmpty(language) && TextUtils.isEmpty(area)) {
        // 如果语言和地区都是空，那么跟随系统
        MMKVUtils.mInstance.encode(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_LANGUAGE, "")
        MMKVUtils.mInstance.encode(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_AREA, "")
    } else {
        // 不为空，修改app语言，持久化语言选项信息
        val newLocale = Locale(language, area)
        saveLanguageSetting(context, newLocale)
        setAppLanguage(context, newLocale)
    }
}

/**
 * 设置语言信息
 *
 * 说明：
 * 该方法建议在attachBaseContext和onConfigurationChange中调用，attachBaseContext可以保证页面加载时修改语言信息，
 * 而onConfigurationChange则是为了对应横竖屏切换时系统更新Resource的问题
 *
 * @param context application context
 */
fun setConfiguration(context: Context) {
    // 为防止传入非ApplicationContext，这里做一次强制转化，目的是避免onConfigurationChange可能导致的问题，
    // 因为onConfigurationChange被触发时系统会更新ApplicationContext中的Resource，如果页面包含Runtime资源
    // （运行时动态加载的资源）时，有可能语言显示不一致。
    val appContext = context.applicationContext
    val preferredLocale = getSysPreferredLocale()
    val configuration = appContext.resources.configuration
    if (Build.VERSION.SDK_INT >= 17) {
        configuration.setLocale(preferredLocale)
    } else {
        configuration.locale = preferredLocale
    }
    // 更新context中的语言设置
    val resources = appContext.resources
    val dm = resources.displayMetrics
    resources.updateConfiguration(configuration, dm)
}

/**
 * 更新应用语言（核心）
 * @param context
 * @param locale
 */
fun setAppLanguage(context: Context, locale: Locale) {
    Log.e("xiang", "==设置语言${locale.language}_${locale.country}")
    val resources = context.resources
    val metrics = resources.displayMetrics
    val configuration = resources.configuration
    // Android 7.0以上的方法
    if (Build.VERSION.SDK_INT >= 24) {
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList(locale))
        context.createConfigurationContext(configuration)
        // 实测，updateConfiguration这个方法虽然很多博主说是版本不适用
        // 但是我的生产环境androidX+Android Q环境下，必须加上这一句，才可以通过重启App来切换语言
        resources.updateConfiguration(configuration, metrics)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        //Android 4.1 以上方法
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, metrics)
    } else {
        configuration.locale = locale
        resources.updateConfiguration(configuration, metrics)
    }
}

/**
 * 切换语言
 */
fun changeAPPLanguage(context: Context, isChinese: Boolean) {
    if (isChinese) {
        changeLanguage(context, "zh", "ZH")
    } else {
        changeLanguage(context, "en", "US")
    }
    BaseApplication.getInstance().recreateAllActivity()
}

/**
 * 切换版本(标准版&简版)
 */
fun changeAppVersion(isSimple: Boolean) {
    MMKVUtils.mInstance.encode(MMKVAlias.CURRENT_APP_VERSION_STYLE, isSimple)
}

/**
 * 当前是否是简版
 */
fun currentAppIsSimpleVersion(): Boolean {
    return MMKVUtils.mInstance.decodeBoolean(MMKVAlias.CURRENT_APP_VERSION_STYLE) ?: false
}

/**
 * 当前是否是英文
 */
fun currentLanguageIsEnglish(context: Context): Boolean {
    return "US" == MMKVUtils.mInstance.decodeString(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_AREA)
}

/**
 * 当前是否是中文
 */
fun currentLanguageIsChinese(): Boolean {
    return "ZH" == MMKVUtils.mInstance.decodeString(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_AREA)
}

/**
 * 当前语言
 */
fun currentLanguage(): String {
    return MMKVUtils.mInstance.decodeStringDef(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_LANGUAGE, "zh")
        ?: "zh"
}

/**
 * 当前国家
 */
fun currentCountry(): String {
    return MMKVUtils.mInstance.decodeStringDef(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_AREA, "ZH") ?: "ZH"
}

/**
 * 清除语言设置
 */
fun clearLanguageSetting() {
    MMKVUtils.mInstance.removeKey(MMKVAlias.CURRENT_APP_VERSION_STYLE)
    MMKVUtils.mInstance.removeKey(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_LANGUAGE)
    MMKVUtils.mInstance.removeKey(MMKVAlias.APP_CHANGE_LANGUAGE_FOR_AREA)
}