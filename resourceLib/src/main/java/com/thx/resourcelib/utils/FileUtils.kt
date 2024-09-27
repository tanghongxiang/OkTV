package com.thx.resourcelib.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ProviderInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.lang.reflect.Method


/**
 * 获取uri对应的绝对路径
 * @param context
 * @param uri
 * @return
 */
fun getRealFilePath(context: Context, uri: Uri?): String? {
    if (null == uri) return null
    val scheme = uri.scheme
    var data: String? = null
    if (scheme == null) data = uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
        data = uri.path
    } else if (ContentResolver.SCHEME_CONTENT == scheme) {
        val cursor = context.contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.ImageColumns.DATA),
            null,
            null,
            null
        )
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (index > -1) {
                    data = cursor.getString(index)
                }
            }
            cursor.close()
        }
    }
    return data ?: getFPUriToPath(context, uri)
}

/**
 * 获取FileProvider path
 */
@SuppressLint("QueryPermissionsNeeded")
private fun getFPUriToPath(context: Context, uri: Uri): String? {
    try {
        val packs =
            context.packageManager.getInstalledPackages(PackageManager.GET_PROVIDERS)
        if (packs != null) {
            val fileProviderClassName: String = FileProvider::class.java.getName()
            for (pack in packs) {
                val providers: Array<ProviderInfo> = pack.providers
                if (providers != null) {
                    for (provider in providers) {
                        if (uri.authority.equals(provider.authority)) {
                            if (provider.name.lowercase() == fileProviderClassName.lowercase()) {
                                val fileProviderClass: Class<FileProvider> =
                                    FileProvider::class.java
                                try {
                                    val getPathStrategy: Method =
                                        fileProviderClass.getDeclaredMethod(
                                            "getPathStrategy",
                                            Context::class.java,
                                            String::class.java
                                        )
                                    getPathStrategy.setAccessible(true)
                                    val invoke: Any =
                                        getPathStrategy.invoke(null, context, uri.authority)
                                    if (invoke != null) {
                                        val PathStrategyStringClass: String =
                                            FileProvider::class.java.name + "\$PathStrategy"
                                        val PathStrategy =
                                            Class.forName(PathStrategyStringClass)
                                        val getFileForUri: Method =
                                            PathStrategy.getDeclaredMethod(
                                                "getFileForUri",
                                                Uri::class.java
                                            )
                                        getFileForUri.setAccessible(true)
                                        val invoke1: Any = getFileForUri.invoke(invoke, uri)
                                        if (invoke1 is File) {
                                            return (invoke1 as File).getAbsolutePath()
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                break
                            }
                            break
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}


fun drawableToBitamp(drawable: Drawable?): Bitmap? {
    if(drawable==null)return null
    //声明将要创建的bitmap
    var bitmap: Bitmap? = null
    //获取图片宽度
    val width = drawable.intrinsicWidth
    //获取图片高度
    val height = drawable.intrinsicHeight
    //图片位深，PixelFormat.OPAQUE代表没有透明度，RGB_565就是没有透明度的位深，否则就用ARGB_8888。详细见下面图片编码知识。
    val config =
        if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
    //创建一个空的Bitmap
    bitmap = Bitmap.createBitmap(width, height, config)
    //在bitmap上创建一个画布
    val canvas = Canvas(bitmap)
    //设置画布的范围
    drawable.setBounds(0, 0, width, height)
    //将drawable绘制在canvas上
    drawable.draw(canvas)
    return bitmap
}




/**
 * 拍照、裁剪图片地址
 *
 * @return
 */
fun getSandboxPath(context: Context): String {
    val externalFilesDir = context.getExternalFilesDir("")
    val customFile = File(externalFilesDir!!.absolutePath, "OkTV")
    if (!customFile.exists()) {
        customFile.mkdirs()
    }
    return customFile.absolutePath + File.separator
}

/**
 * 图片压缩后的路径
 */
fun zipImgPathDir(context: Context, childDir: String = "/Pictures/zip/"): String {
    val path = context.getExternalFilesDir(null)?.absolutePath?.toString() + childDir
    val file = File(path)
    if (file.mkdirs()) {
        return path
    }
    return path
}
