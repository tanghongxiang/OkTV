package com.thx.resourcelib.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.thx.resourcelib.R

/**
 * @Description:
 * @Author: tanghongxiang
 * @Version: V1.00
 * @since 2021/12/01 21:25
 */
class HomeRefreshHeaderLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), RefreshHeader {

    /** 当前ico缩放比例 */
    private var scale = 0f

    private var tempScaleVar = 0f

    private var isDragging = false

    private var objectAnimator: ObjectAnimator? = null

    private var isFinish = false
    var l: OnMoveListener? = null

    init {
        View.inflate(context, R.layout.widget_refresh_header_layout, this)
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) = when (newState) {
        RefreshState.PullDownToRefresh -> {
            this.isFinish = false
        }

        RefreshState.RefreshFinish -> {
            objectAnimator?.cancel()
            this.isFinish = true
        }

        else -> {
        }
    }

    /**
     * 获取真实视图（必须返回，不能为null）
     */
    override fun getView(): View = this

    override fun getSpinnerStyle(): SpinnerStyle = SpinnerStyle.Translate

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
        this.l?.onMove(isDragging, percent, offset, height, maxDragHeight)
        this.isDragging = isDragging
        runScaleXYAnim(percent)
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        runScaleXAnim()
    }

    /**
     * @return 延迟500毫秒之后再弹回
     */
    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean) = 0

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun isSupportHorizontalDrag() = false

    override fun autoOpen(duration: Int, dragRate: Float, animationOnly: Boolean): Boolean {
        return true
    }

    private fun runScaleXAnim() {
        objectAnimator = ObjectAnimator.ofFloat(findViewById(R.id.headerIco), "scaleX", 1f, 0f)
            .setDuration(600)
        objectAnimator?.repeatMode = ValueAnimator.REVERSE
        objectAnimator?.repeatCount = ValueAnimator.INFINITE
        objectAnimator?.start()
    }

    private fun runScaleXYAnim(percent: Float) {
        tempScaleVar = if (percent > 1) 1f else percent
        val xAnim =
            ObjectAnimator.ofFloat(findViewById(R.id.headerIco), "scaleX", scale, tempScaleVar)
        val yAnim =
            ObjectAnimator.ofFloat(findViewById(R.id.headerIco), "scaleY", scale, tempScaleVar)
        val animatorSet = AnimatorSet()
        animatorSet.play(xAnim).with(yAnim)
        animatorSet.duration = 0
        animatorSet.start()
        this.scale = tempScaleVar
    }

    interface OnMoveListener {
        fun onMove(
            isDragging: Boolean,
            percent: Float,
            offset: Int,
            height: Int,
            maxDragHeight: Int
        )
    }

    fun setListener(l: OnMoveListener) {
        this.l = l
    }

}