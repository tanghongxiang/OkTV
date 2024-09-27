package com.thx.resourcelib.ext

/** 返回值为一个没有参数类型的函数 */
typealias CommonCallBackListener = () -> Unit

/** 返回值为单个Int的函数类型 */
typealias IntCallBackListener = (Int) -> Unit

/** 返回值为单个Boolean的函数类型 */
typealias BooleanCallBackListener = (Boolean) -> Unit

/** 返回值为单个Float的函数类型 */
typealias FloatCallBackListener = (Float) -> Unit

/** 返回值为单个Double的函数类型 */
typealias DoubleCallBackListener = (Double) -> Unit

/** 返回值为单个String的函数类型 */
typealias StringCallBackListener = (String) -> Unit


/**
 * 普通业务逻辑处理结果返回
 */
sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
