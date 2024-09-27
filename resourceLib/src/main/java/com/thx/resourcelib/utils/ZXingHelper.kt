package com.thx.resourcelib.utils

import android.graphics.Bitmap
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder

/**
 * 同步创建指定前景色、指定背景色、带logo的二维码图片。该方法是耗时操作，请在子线程中调用。
 *
 * @param content         要生成的二维码图片内容
 * @param size            图片宽高，单位为px
 * @param foregroundColor 二维码图片的前景色
 * @param backgroundColor 二维码图片的背景色
 * @param logo            二维码图片的logo
 */
fun createQRCode(
    content: String,
    size: Int,
    foregroundColor: Int,
    backgroundColor: Int,
    logo: Bitmap?
): Bitmap? {
    return try {
        QRCodeEncoder.syncEncodeQRCode(content, size, foregroundColor, backgroundColor, logo)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

