package com.thx.resourcelib.ext

import android.app.Activity
import android.view.View
import com.thx.resourcelib.utils.DisplayUtils

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2022/9/5 6:18 下午
 */

/**
 * 设置View顶部边距为底部状态栏高度
 */
fun setViewPaddingStatus(view: View, context: Activity) {
    view.setPadding(view.paddingStart, DisplayUtils.getStatusHeight(context) + view.paddingTop, view.paddingEnd, view.paddingBottom)
}

/**
 * 设置View底部边距为底部状态栏高度
 */
fun setViewPaddingNavigation(view: View, context: Activity) {
    view.setPadding(
        view.paddingStart,
        view.paddingTop,
        view.paddingEnd,
        DisplayUtils.getNavigationBarHeight(context) + view.paddingBottom
    )
}

/**
 * 设置View底部内边距
 */
fun View.setViewPaddingBottom(bottom: Int) {
    setPadding(
        this.paddingStart ,
        this.paddingTop,
        this.paddingEnd ,
        bottom
    )
}

/**
 * 设置View顶部内边距
 */
fun View.setViewPaddingTop(top: Int) {
    setPadding(
        this.paddingStart ,
        top,
        this.paddingEnd ,
        this.paddingBottom
    )
}
