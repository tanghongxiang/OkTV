package com.thx.resourcelib.ext

import android.app.Activity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.thx.resourcelib.base.BaseApplication

/**
 * 打开一个新的Activity
 */
fun openNewActivity(path: String, params: Bundle? = null) {
    val mPostcard = ARouter.getInstance().build(path)
    if (params != null) {
        mPostcard.with(params)
    }
    mPostcard.navigation()
}

/**
 * 打开一个新的Activity带动画
 */
fun openNewActivityWithAnim(path: String, params: Bundle? = null, startAnim: Int, endAnim: Int) {
    val mPostcard = ARouter.getInstance().build(path)
    if (params != null) {
        mPostcard.with(params)
    }
    mPostcard.withTransition(startAnim,endAnim)
    mPostcard.navigation(BaseApplication.getInstance().topActivity())
}

/**
 * 打开一个新的Activity(带结果)
 */
fun openNewActivityForResult(
    context: Activity,
    path: String,
    params: Bundle? = null,
    requestCode: Int
) {
    val mPostcard = ARouter.getInstance().build(path)
    if (params != null) {
        mPostcard.with(params)
    }
    mPostcard.navigation(context, requestCode)
}
/**
 * 打开一个新的Activity(带动画)
 */
fun openNewActivityWithOptionsCompat(
    context: Activity,
    path: String,
    params: Bundle? = null,
    optionsCompat: ActivityOptionsCompat? = null
) {
    val mPostcard = ARouter.getInstance().build(path)
    if (params != null) {
        mPostcard.with(params)
    }
    if (optionsCompat != null) {
        mPostcard.withOptionsCompat(optionsCompat).navigation(context)
    } else {
        mPostcard.navigation()

    }
}