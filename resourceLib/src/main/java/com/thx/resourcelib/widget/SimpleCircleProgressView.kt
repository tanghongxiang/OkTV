package com.thx.resourcelib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.thx.resourcelib.base.BaseApplication
import com.thx.resourcelib.utils.DisplayUtils

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/7/25 下午5:49
 */
class SimpleCircleProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /** 背景画笔 */
    private val bgProgressPaint: Paint = Paint()

    /** 进度条画笔 */
    private val progressPaint: Paint = Paint()

    /** 当前进度(0~1) */
    private var mCurrentProgress = 0f

    /** 进度条宽度 */
    private var progressLineWidth: Float =
        DisplayUtils.convertDp2Pixel(2f, BaseApplication.getInstance())

    init {
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = progressLineWidth
        progressPaint.color = Color.parseColor("#3D465A")
        progressPaint.isAntiAlias = true
        progressPaint.strokeCap = Paint.Cap.ROUND

        bgProgressPaint.style = Paint.Style.STROKE
        bgProgressPaint.strokeWidth = progressLineWidth
        bgProgressPaint.color = Color.parseColor("#C6C9D0")
        bgProgressPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBgCircle(canvas)
        drawProgress(canvas)
    }

    /**
     * 绘制背景圆
     */
    private fun drawBgCircle(canvas: Canvas) {
        canvas.drawArc(
            RectF(
                bgProgressPaint.strokeWidth,
                bgProgressPaint.strokeWidth,
                measuredWidth.toFloat() - bgProgressPaint.strokeWidth,
                measuredHeight.toFloat() - bgProgressPaint.strokeWidth
            ),
            0f,
            360f,
            false,
            bgProgressPaint
        )
    }

    /**
     * 绘制进度
     */
    private fun drawProgress(canvas: Canvas) {
        canvas.drawArc(
            RectF(
                progressPaint.strokeWidth,
                progressPaint.strokeWidth,
                measuredWidth.toFloat() - progressPaint.strokeWidth,
                measuredHeight.toFloat() - progressPaint.strokeWidth
            ),
            0f,
            360 * mCurrentProgress,
            false,
            progressPaint
        )
    }

    /** 更新进度 */
    fun updateProgress(progress: Float) {
        this.mCurrentProgress = when {
            progress < 0f -> 0f
            progress > 1f -> 1f
            else -> progress
        }
        invalidate()
    }


}