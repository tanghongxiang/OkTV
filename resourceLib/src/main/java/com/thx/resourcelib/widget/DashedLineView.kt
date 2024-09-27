package com.thx.resourcelib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/8/23 下午6:06
 */
class DashedLineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint: Paint = Paint()

    init {
        paint.setColor(
            ContextCompat.getColor(context, com.thx.resourcelib.R.color.color_A6A6A6)
        )
        paint.style = Paint.Style.STROKE
        paint.setPathEffect(DashPathEffect(floatArrayOf(18f, 10f), 0f))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
    }

}