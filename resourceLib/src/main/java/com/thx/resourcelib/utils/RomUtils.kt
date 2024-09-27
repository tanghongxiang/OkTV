package com.thx.resourcelib.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*


/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2022/9/6 2:04 下午
 */
object RomUtils {

    private const val TAG = "RomUtils--->"

    /**
     * 获取 emui 版本号
     */
    @JvmStatic
    fun getEmuiVersion(): Double {
        try {
            val emuiVersion = getSystemProperty("ro.build.version.emui")
            val version = emuiVersion!!.substring(emuiVersion.indexOf("_") + 1)
            return version.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 4.0
    }

    @JvmStatic
    fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: Exception) {
            Log.e(TAG, "Unable to read sysprop $propName", ex)
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: Exception) {
                    Log.e(TAG, "Exception while closing InputStream", e)
                }
            }
        }
        return line
    }

    @JvmStatic
    fun checkIsHuaweiRom() = Build.MANUFACTURER.contains("HUAWEI")

    @JvmStatic
    fun checkIsMiuiRom() = !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"))

    fun checkIsMeizuRom(): Boolean {
        val systemProperty = getSystemProperty("ro.build.display.id")
        return if (TextUtils.isEmpty(systemProperty)) false
        else systemProperty!!.contains("flyme") || systemProperty.lowercase(Locale.getDefault())
            .contains("flyme")
    }

    fun checkIs360Rom(): Boolean =
        Build.MANUFACTURER.contains("QiKU") || Build.MANUFACTURER.contains("360")

    fun checkIsOppoRom() =
        Build.MANUFACTURER.contains("OPPO") || Build.MANUFACTURER.contains("oppo")

    fun checkIsVivoRom() =
        Build.MANUFACTURER.contains("VIVO") || Build.MANUFACTURER.contains("vivo")


    /**
     * 华为手机是否隐藏了虚拟导航栏
     * @return true 表示隐藏了，false 表示未隐藏
     */
    private fun isHuaWeiHideNav(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Settings.System.getInt(context.contentResolver, "navigationbar_is_min", 0)
        } else {
            Settings.Global.getInt(context.contentResolver, "navigationbar_is_min", 0)
        } != 0

    /**
     * 小米手机是否开启手势操作
     * @return false 表示使用的是虚拟导航键(NavigationBar)， true 表示使用的是手势， 默认是false
     */
    private fun isMiuiFullScreen(context: Context) =
        Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0) != 0

    /**
     * Vivo手机是否开启手势操作
     * @return false 表示使用的是虚拟导航键(NavigationBar)， true 表示使用的是手势， 默认是false
     */
    private fun isVivoFullScreen(context: Context): Boolean =
        Settings.Secure.getInt(context.contentResolver, "navigation_gesture_on", 0) != 0


    /**
     * 判断虚拟导航栏是否显示
     *
     * @param context 上下文对象
     * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
     */
    fun hasNavigationBar(act: Activity): Boolean {
        return navigationGestureEnabled(act)
//        val windowManager = act.getSystemService(Service.WINDOW_SERVICE) as WindowManager
//        val display = windowManager.defaultDisplay
//        val point = Point()
//        display.getRealSize(point)
//        return point.y!=navHeight
    }

    /**
     * 获取主流手机设置中的"navigation_gesture_on"值，判断当前系统是使用导航键还是手势导航操作
     * @param context app Context
     * @return
     * false 表示使用的是虚拟导航键(NavigationBar)，
     * true 表示使用的是手势， 默认是false
     */
    private fun navigationGestureEnabled(context: Context): Boolean {
        var enable = Settings.Global.getInt(context.contentResolver, getDeviceInfo(), 0)
        val brand = Build.BRAND
        //0和1是虚拟按键模式，2和3是手势模式
        if (!TextUtils.isEmpty(brand) && (brand.equals(
                "OPPO",
                ignoreCase = true
            ) || brand.equals("VIVO", ignoreCase = true))
        ) {
            enable = Settings.Secure.getInt(context.contentResolver, getDeviceInfo(), 2)
        }
        return enable != 0
    }

    /**
     * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo、三星都可以）
     * @return
     */
    private fun getDeviceInfo(): String {
        val brand = Build.BRAND
        if (TextUtils.isEmpty(brand)) return "navigationbar_is_min"
        return if (brand.equals("HUAWEI", ignoreCase = true) || "HONOR" == brand) {
            "navigationbar_is_min"
        } else if (brand.equals("XIAOMI", ignoreCase = true)) {
            "force_fsg_nav_bar"
        } else if (brand.equals("VIVO", ignoreCase = true)) {
            "navigation_gesture_on"
        } else if (brand.equals("OPPO", ignoreCase = true)) {
            "hide_navigationbar_enable"
        } else if (brand.equals("samsung", ignoreCase = true)) {
            "navigationbar_hide_bar_enabled"
        } else if (brand.equals("Nokia", ignoreCase = true)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                "navigation_bar_can_hiden"
            } else {
                "swipe_up_to_switch_apps_enabled"
            }
        } else {
            "navigationbar_is_min"
        }
    }


}