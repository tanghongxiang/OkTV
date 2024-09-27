package com.thx.resourcelib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.thx.resourcelib.R
import com.thx.resourcelib.utils.DisplayUtils

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2023/10/22 7:14 PM
 */
class RoundRectImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    var radius = -1f

    init {
        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.RoundRectImageView)
            radius = 1f * array.getDimensionPixelOffset(R.styleable.RoundRectImageView_round_img_radius, -1)
            array.recycle();
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (radius <= 0) {
            radius = DisplayUtils.convertDp2Pixel(4f, context)
        }
        if (width > radius && height > radius) {
            val path = Path()
            path.moveTo(radius, 0f)
            path.lineTo(measuredWidth.toFloat() - radius, 0f)
            path.quadTo(measuredWidth.toFloat(), 0f, measuredWidth.toFloat(), radius)

            path.lineTo(measuredWidth.toFloat(),measuredHeight.toFloat()-radius)
            path.quadTo(measuredWidth.toFloat(), measuredHeight.toFloat(), measuredWidth.toFloat()-radius, measuredHeight.toFloat())
            path.lineTo(radius,measuredHeight.toFloat())
            path.quadTo(0f,measuredHeight.toFloat(),0f,measuredHeight.toFloat()-radius)
            path.lineTo(0f,radius)
            path.quadTo(0f,0f,radius,0f)
//            path.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat())
//            path.lineTo(0f, measuredHeight.toFloat())
//            path.lineTo(0f, radius)
//            path.quadTo(0f,0f,radius,0f)
            canvas?.clipPath(path)
        }
        super.onDraw(canvas)
    }

}
