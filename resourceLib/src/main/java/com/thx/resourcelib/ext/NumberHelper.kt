package com.thx.resourcelib.ext

import java.text.DecimalFormat

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2022/8/29 7:41 下午
 */

/** 补全两位数字 */
fun Int.doubleNum(): String = if (this < 10) "0$this" else "$this"

/** 补全两位数字 */
fun Long.doubleNum(): String = if (this < 10) "0$this" else "$this"

/** 去除两位小数末尾0 */
fun String.delDoubleNumEndZero(): String {
    if (this.contains(".")) {
        if (this.endsWith(".0")) {
            return this.replace(".0", "")
        }
        if (this.endsWith(".00")) {
            return this.replace(".00", "")
        }
        if (this.endsWith("0") && this.length > 1) {
            return this.substring(0, this.length - 1)
        }
        return this
    } else {
        return this
    }
}

/**
 * 文件大小转换成KB,MB,GB
 */
fun Long.convertFileSize(): String {
    val bytes = StringBuffer()
    val format = DecimalFormat("###.0")
    if (this >= 1024 * 1024 * 1024) {
        val i = this / (1024.0 * 1024.0 * 1024.0)
        bytes.append(format.format(i)).append("GB")
    } else if (this >= 1024 * 1024) {
        val i = this / (1024.0 * 1024.0)
        bytes.append(format.format(i)).append("MB")
    } else if (this >= 1024) {
        val i = this / 1024.0
        bytes.append(format.format(i)).append("KB")
    } else if (this < 1024) {
        if (this <= 0) {
            bytes.append("0B")
        } else {
            bytes.append(this.toInt()).append("B")
        }
    }
    return bytes.toString()
}

/**
 * 数字逗号分割
 */
fun String.formatNumber(): String {
    if (this.length < 3 && !this.contains(".")) return this
    var leftNum = ""
    var rightNum = ""
    if (this.contains(".")) {
        val arr = this.split(".")
        leftNum = arr[0]
        rightNum = arr[1]
    } else {
        leftNum = this
    }
    if (leftNum.length < 3) return this
    val sb = StringBuilder()
    val arr = leftNum.toCharArray()
    for (item in arr.size - 1 downTo 0) {
        sb.insert(0, arr[item])
        if ((arr.size - item) % 3 == 0 && item != 0) {
            sb.insert(0, ",")
        }
    }
    return sb.toString() + if (this.contains(".")) ".$rightNum" else rightNum
}

/**
 * 格式换两位小数
 */
fun Double.formatDoubleDecimal(): String {
    val format = DecimalFormat("#0.00")
    return format.format(this)
}
