package com.thx.oktv.widget

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.TextureView

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2022/8/1 3:07 下午
 */
class AutoTextureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextureView(context, attrs) {

    fun updateTextureViewSizeCenter(mVideoWidth: Int, mVideoHeight: Int) {
        val sx = width.toFloat() / mVideoWidth
        val sy = height.toFloat() / mVideoHeight
        val matrix = Matrix()

        //第1步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((width - mVideoWidth) / 2f, (height - mVideoHeight) / 2f)

        //第2步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(mVideoWidth / width.toFloat(), mVideoHeight / height.toFloat())

        //第3步,等比例放大或缩小,直到视频区的一边和View一边相等.如果另一边和view的一边不相等，则留下空隙
        if (sx >= sy) {
            matrix.postScale(sy, sy, width / 2f, height / 2f)
        } else {
            matrix.postScale(sx, sx, width / 2f, height / 2f)
        }
        setTransform(matrix)
        postInvalidate()
    }

    fun updateTextureViewSizeCenter(width:Float,height:Float,mVideoWidth: Int, mVideoHeight: Int) {
        val sx = width / mVideoWidth
        val sy = height / mVideoHeight
        val matrix = Matrix()

        //第1步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((width - mVideoWidth) / 2f, (height - mVideoHeight) / 2f)

        //第2步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(mVideoWidth / width, mVideoHeight / height.toFloat())

        //第3步,等比例放大或缩小,直到视频区的一边和View一边相等.如果另一边和view的一边不相等，则留下空隙
        if (sx >= sy) {
            matrix.postScale(sy, sy, width / 2f, height / 2f)
        } else {
            matrix.postScale(sx, sx, width / 2f, height / 2f)
        }
        setTransform(matrix)
        postInvalidate()
    }


}