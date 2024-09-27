package com.thx.resourcelib.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2022/7/21 1:47 下午
 */
/**
 * 获取两个色值的中间色
 * @param fraction
 * @param startColor
 * @param endColor
 * @return
 */
fun getPercentColor(fraction: Float, startColor: Int, endColor: Int): Int {
    val redCurrent: Int
    val blueCurrent: Int
    val greenCurrent: Int
    val alphaCurrent: Int
    val redStart = Color.red(startColor)
    val blueStart = Color.blue(startColor)
    val greenStart = Color.green(startColor)
    val alphaStart = Color.alpha(startColor)
    val redEnd = Color.red(endColor)
    val blueEnd = Color.blue(endColor)
    val greenEnd = Color.green(endColor)
    val alphaEnd = Color.alpha(endColor)
    val redDifference = redEnd - redStart
    val blueDifference = blueEnd - blueStart
    val greenDifference = greenEnd - greenStart
    val alphaDifference = alphaEnd - alphaStart
    redCurrent = (redStart + fraction * redDifference).toInt()
    blueCurrent = (blueStart + fraction * blueDifference).toInt()
    greenCurrent = (greenStart + fraction * greenDifference).toInt()
    alphaCurrent = (alphaStart + fraction * alphaDifference).toInt()
    return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent)
}

/**
 * 修改图标颜色
 */
fun changeImgColor(
    context: Context,
    @DrawableRes resourceID: Int,
    startColor: Int,
    endColor: Int,
    percent: Float
): Drawable? {
    val up = ContextCompat.getDrawable(context, resourceID)
    if (up != null) {
        val drawableUp = DrawableCompat.wrap(up)
        DrawableCompat.setTint(drawableUp, getPercentColor(percent, startColor, endColor))
        return drawableUp
    }
    return ContextCompat.getDrawable(context, resourceID)
}

/**
 * 修改图标颜色
 */
fun changeImgColor(
    up: Drawable?,
    startColor: Int,
    endColor: Int,
    percent: Float
): Drawable? {
    if (up != null) {
        val drawableUp = DrawableCompat.wrap(up)
        DrawableCompat.setTint(drawableUp, getPercentColor(percent, startColor, endColor))
        return drawableUp
    }
    return up
}

/**
 * 根据颜色名称获取对应色值
 * @des <0表示获取色值失败
 */
fun Context.findColorByName(str:String):Int{
    return resources.getIdentifier(str, "color", packageName)
}