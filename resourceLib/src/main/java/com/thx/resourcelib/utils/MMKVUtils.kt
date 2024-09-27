package com.thx.resourcelib.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.tencent.mmkv.MMKV
import com.thx.resourcelib.base.BaseApplication

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/25 3:44 PM
 */
class MMKVUtils {

    private var mmkv: MMKV? = null

    private var sp: SharedPreferences? = null

    init {
        // MMKV在部分机型中可能会初始化失败，报so没找到，所以用SharedPreferences作兜底
        try {
            mmkv = MMKV.defaultMMKV()
        } catch (e: Exception) {
            sp =
                BaseApplication.getInstance().getSharedPreferences("amber_sp", Context.MODE_PRIVATE)
        }
    }

    companion object {
        val mInstance: MMKVUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MMKVUtils()
        }
    }

    fun encode(key: String, value: Any?) {

        when (value) {

            is String -> {
                mmkv?.encode(key, value)
                sp?.edit()?.putString(key, value)?.commit()
            }

            is Float -> {
                mmkv?.encode(key, value)
                sp?.edit()?.putFloat(key, value)?.commit()
            }

            is Boolean -> {
                mmkv?.encode(key, value)
                sp?.edit()?.putBoolean(key, value)?.commit()
            }

            is Int -> {
                mmkv?.encode(key, value)
                sp?.edit()?.putInt(key, value)?.commit()
            }

            is Long -> {
                mmkv?.encode(key, value)
                sp?.edit()?.putLong(key, value)?.commit()
            }

            is Double -> {
                mmkv?.encode(key, value)
            }

            is ByteArray -> {
                mmkv?.encode(key, value)
            }

        }

    }

    fun decodeInt(key: String): Int? {
        return mmkv?.decodeInt(key, 0) ?: sp?.getInt(key, 0)
    }

    fun decodeDouble(key: String): Double? {
        return mmkv?.decodeDouble(key, 0.00)
    }

    fun decodeLong(key: String): Long? {
        return mmkv?.decodeLong(key, 0L) ?: sp?.getLong(key, 0L)
    }

    fun decodeBoolean(key: String): Boolean? {
        return mmkv?.decodeBool(key, false) ?: sp?.getBoolean(key, false)
    }

    fun decodeBooleanTrue(key: String): Boolean? {
        return mmkv?.decodeBool(key, true) ?: sp?.getBoolean(key, true)
    }

    fun decodeFloat(key: String): Float? {
        return mmkv?.decodeFloat(key, 0F) ?: sp?.getFloat(key, 0f)
    }

    fun decodeByteArray(key: String): ByteArray? {
        return mmkv?.decodeBytes(key)
    }

    fun decodeString(key: String): String? {
        return mmkv?.decodeString(key, "") ?: sp?.getString(key, "")
    }

    fun decodeStringDef(key: String, def: String): String? {
        return mmkv?.decodeString(key, def) ?: sp?.getString(key, def)
    }

    fun removeKey(key: String) {
        mmkv?.removeValueForKey(key)
        sp?.edit()?.remove(key)?.commit()
    }

    fun containsKey(key: String): Boolean {
        return if (mmkv == null) sp?.contains(key) ?: false else mmkv?.containsKey(key) ?: false
    }

    fun clearAll() {
        mmkv?.clearAll()
        sp?.edit()?.clear()?.commit()
    }

}