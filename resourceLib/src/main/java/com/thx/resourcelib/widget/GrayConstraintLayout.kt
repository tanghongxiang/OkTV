package com.thx.resourcelib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout


/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2023/6/6 8:41 PM
 */
open class GrayConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val mPaint = Paint()

    private var isGray = false

    init {
        val cm = ColorMatrix()
        //设置 1：彩色  0：黑白
        cm.setSaturation(1f)
        mPaint.colorFilter = ColorMatrixColorFilter(cm)
    }

    override fun draw(canvas: Canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG)
        super.draw(canvas)
        canvas.restore()
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG)
        super.dispatchDraw(canvas)
        canvas.restore()
    }

    /**
     * @desc 设置灰色页面
     */
    fun setGray(isGray: Boolean) {
        this.isGray = isGray
        val cm = ColorMatrix()
        //设置 1：彩色  0：黑白
        cm.setSaturation((if (isGray) 0 else 1).toFloat())
        mPaint.colorFilter = ColorMatrixColorFilter(cm)
        //重新绘制画布
        invalidate()
    }

    /**
     * 当前是否是灰度布局
     */
    fun isGray() = isGray


}