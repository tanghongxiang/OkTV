package com.thx.resourcelib.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import com.lxj.xpopup.util.KeyboardUtils
import com.lxj.xpopup.util.XPopupUtils
import com.thx.resourcelib.base.BaseApplication
import java.util.*
import kotlin.math.abs

class SoftKeyboardStateHelper @JvmOverloads constructor(
    private val activityRootView: View,
    var isSoftKeyboardOpened: Boolean = false
) : OnGlobalLayoutListener {
    interface SoftKeyboardStateListener {
        fun onSoftKeyboardOpened(keyboardHeightInPx: Int)
        fun onSoftKeyboardClosed()
    }

    private val listeners: MutableList<SoftKeyboardStateListener> = LinkedList()

    /**
     * Default value is zero (0)
     *
     * @return last saved keyboard height in px
     */
    var lastSoftKeyboardHeightInPx = 0
        private set

    init {
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        val r = Rect()
        //r will be populated with the coordinates of your view that area still visible.  
        activityRootView.getWindowVisibleDisplayFrame(r)
        val heightDiff = activityRootView.rootView.height - (r.bottom - r.top)
        if (!isSoftKeyboardOpened && heightDiff > 220) { // if more than 100 pixels, its probably a keyboard...
            isSoftKeyboardOpened = true
            notifyOnSoftKeyboardOpened(heightDiff)
        } else if (isSoftKeyboardOpened && heightDiff < 220) {
            isSoftKeyboardOpened = false
            notifyOnSoftKeyboardClosed()
        }
    }

    fun addSoftKeyboardStateListener(listener: SoftKeyboardStateListener) {
        listeners.add(listener)
    }

    fun removeSoftKeyboardStateListener(listener: SoftKeyboardStateListener) {
        listeners.remove(listener)
    }

    private fun notifyOnSoftKeyboardOpened(keyboardHeightInPx: Int) {
        lastSoftKeyboardHeightInPx = keyboardHeightInPx
        for (listener in listeners) {
            listener.onSoftKeyboardOpened(keyboardHeightInPx)
        }
    }

    private fun notifyOnSoftKeyboardClosed() {
        for (listener in listeners) {
            listener.onSoftKeyboardClosed()
        }
    }

    companion object{

        private var sDecorViewDelta = 0

        fun getDecorViewInvisibleHeight(window: Window): Int {
            val decorView = window.decorView
            val outRect = Rect()
            decorView.getWindowVisibleDisplayFrame(outRect)
            //        Log.d("KeyboardUtils", "getDecorViewInvisibleHeight: "
//                + (decorView.getBottom() - outRect.bottom));
            val delta = abs(decorView.bottom - outRect.bottom)
            if (delta <= XPopupUtils.getNavBarHeight() + XPopupUtils.getStatusBarHeight()) {
                sDecorViewDelta = delta
                return 0
            }
            return delta - sDecorViewDelta
        }

    }
}