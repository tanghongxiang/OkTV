package com.thx.resourcelib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.thx.resourcelib.utils.DisplayUtils


/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2023/8/3 3:22 PM
 */
class CircleProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /** 进度条画笔 */
    private val progressPaint: Paint = Paint()

    /** 文字画笔 */
    private val textPaint: TextPaint = TextPaint()

    /** 当前进度(0~1) */
    private var mCurrentProgress = 0f

    /** 是否自定义进度条宽度及文字大小 */
    private var isSelfRule: Boolean = false

    init {
        progressPaint.style = Paint.Style.STROKE
        progressPaint.color = Color.WHITE
        progressPaint.isAntiAlias = true
        progressPaint.strokeCap = Paint.Cap.ROUND

        textPaint.isAntiAlias = true
        textPaint.color = Color.WHITE
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isSelfRule) {
            progressPaint.strokeWidth = DisplayUtils.convertDp2Pixel(5f, context)
            textPaint.textSize = DisplayUtils.convertDp2Pixel(28f, context)
        }
        canvas?.let {
            drawProgress(it)
            drawCenterText(it)
        }
    }

    /*** 绘制进度 */
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

    /*** 绘制中心文字 */
    private fun drawCenterText(canvas: Canvas) {
        val textBounds = Rect()
        val textContent = "${(mCurrentProgress * 100).toInt()}%"
        textPaint.getTextBounds(textContent, 0, textContent.length, textBounds)
        canvas.drawText(
            textContent,
            measuredWidth / 2f,
            textBounds.height() / 2f + height / 2f,
            textPaint
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

    /**
     * 设置文字大小及线条宽度
     * dp
     */
    fun setProgressLineAndText(textSize: Float, lineWidth: Float) {
        progressPaint.strokeWidth = DisplayUtils.convertDp2Pixel(lineWidth, context)
        textPaint.textSize = DisplayUtils.convertDp2Pixel(textSize, context)
        isSelfRule = true
    }

}