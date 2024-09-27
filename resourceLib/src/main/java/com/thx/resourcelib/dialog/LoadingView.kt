package com.thx.resourcelib.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import com.thx.resourcelib.R
import com.thx.resourcelib.utils.DisplayUtils


/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2022/8/15 5:20 下午
 */
class LoadingView(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    private var mCancelable: Boolean = true


    companion object {

        fun create(act: Activity): LoadingView {
            val dialog = LoadingView(act, R.style.LoadingDialogTheme)
            dialog.setContentView(
                View.inflate(act, R.layout.business_dialog_loading_layout, null)
            )
            val dialogWindow = dialog.window
            dialogWindow?.setGravity(Gravity.CENTER)
//            dialogWindow?.setWindowAnimations(R.style.commonDialogAnim)
            dialogWindow?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val attr = dialogWindow?.attributes
            attr?.width = DisplayUtils.convertDp2Pixel(82f, act).toInt()
            attr?.height = DisplayUtils.convertDp2Pixel(202f, act).toInt()
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        loadingImg.setAnimation(R.raw.loading_lottie)
        setCanceledOnTouchOutside(false)
    }

    override fun setCancelable(flag: Boolean) {
        this.mCancelable = flag
        super.setCancelable(flag)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (KeyEvent.KEYCODE_BACK == event.keyCode && !mCancelable) {
            findActivity(context)?.finish()
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    private fun findActivity(context: Context?): Activity? {
        if (context is Activity) {
            return context
        }
        return if (context is ContextWrapper) {
            findActivity(context.baseContext)
        } else {
            null
        }
    }

}