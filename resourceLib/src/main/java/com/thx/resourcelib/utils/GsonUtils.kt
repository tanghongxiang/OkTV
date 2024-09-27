package com.thx.resourcelib.utils

import com.google.gson.Gson
import com.google.gson.JsonParser

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/25 3:49 PM
 */
class GsonUtils {

    private var gson = Gson()

    companion object {

        val mInstance: GsonUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GsonUtils()
        }
    }

    /**
     * json string 转对象
     */
    fun <T> toObject(str: String, clazz: Class<T>): T? {
        return try {
            gson.fromJson(str, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Json string 转ArrayList
     */
    fun <T> toArrayList(str: String, clazz: Class<T>): ArrayList<T>? {
        val dataList = arrayListOf<T>()
        val jsonArray = try {
            JsonParser.parseString(str).asJsonArray
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        if (jsonArray.size() > 0) {
            jsonArray.forEach {
                val item = toObject(it.toString(), clazz)
                item?.let { dataList.add(item) }
            }
        }
        return dataList
    }

    /**
     * 对象转json串
     */
    fun toJsonString(obj: Any?): String {
        return try {
            if (obj == null) "" else gson.toJson(obj)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

}