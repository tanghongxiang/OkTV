package com.thx.resourcelib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.thx.resourcelib.utils.DisplayUtils

/**
 * @Description:圆角图片，待拓展...
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2023/7/12 10:50 AM
 */
class RoundImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    var radius = -1f

    override fun onDraw(canvas: Canvas) {
        if (radius < 0) {
            radius = DisplayUtils.convertDp2Pixel(5f, context)
        }
        if (width > radius && height > radius) {
            val path = Path()
            path.moveTo(radius, 0f)
            path.lineTo(measuredWidth.toFloat() - radius, 0f)
            path.quadTo(measuredWidth.toFloat(), 0f, measuredWidth.toFloat(), radius)

            path.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat())
            path.lineTo(0f, measuredHeight.toFloat())
            path.lineTo(0f, radius)
            path.quadTo(0f,0f,radius,0f)

//            path.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat() - radius)
//            path.quadTo(
//                measuredWidth.toFloat(),
//                measuredHeight.toFloat(),
//                measuredWidth.toFloat() - radius,
//                measuredHeight.toFloat()
//            )
//            path.lineTo(radius, measuredHeight.toFloat())
//            path.quadTo(0f, measuredHeight.toFloat(), 0f, measuredHeight.toFloat() - radius)
//            path.lineTo(0f, radius)
//            path.quadTo(0f, 0f, radius, 0f)
            canvas?.clipPath(path)
        }
        super.onDraw(canvas)
    }

}