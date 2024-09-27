package com.thx.resourcelib.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.thx.resourcelib.BuildConfig
import com.thx.resourcelib.base.BaseApplication
import com.thx.resourcelib.utils.zipImgPathDir
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.Locale

/**
 * 压缩图片
 */
fun zipImg(
    mini: Boolean,
    context: Context,
    filePath: String,
    ignoreBy: Int,
    callback: (compressFile: File?) -> Unit
) {
    if (mini) {
        zipImgForWithAndHeight(context, filePath) {
            luBanZip(context, it.absolutePath, ignoreBy, callback)
        }
    } else {
        luBanZip(context, filePath, ignoreBy, callback)
    }
}

/**
 * 鲁班压缩
 */
private fun luBanZip(
    context: Context,
    filePath: String,
    ignoreBy: Int,
    callback: (compressFile: File?) -> Unit
) {
    Luban.with(context).load(filePath).ignoreBy(ignoreBy)
        .setTargetDir(zipImgPathDir(context, "/share/zip/")).filter {
            !(TextUtils.isEmpty(filePath) || filePath.lowercase(Locale.getDefault())
                .endsWith(".gif"))
        }.setCompressListener(object : OnCompressListener {
            override fun onStart() {
            }

            override fun onSuccess(index: Int, compressFile: File?) {
                if (BuildConfig.DEBUG) {
                    Log.e("xiang", "压缩前大小:${File(filePath).length()}，路径：$filePath,")
                    Log.e(
                        "xiang",
                        "压缩后大小:${compressFile?.length()},路径：${compressFile?.absolutePath}"
                    )
                }
                if (compressFile != null && compressFile.exists()) {
                    callback.invoke(compressFile)
                } else {
                    callback.invoke(File(filePath))
                }
            }

            override fun onError(index: Int, e: Throwable?) {
                // 图片压缩失败
                callback.invoke(File(filePath))
            }

        }).launch()
}

/**
 * 图片宽高压缩
 */
fun zipImgForWithAndHeight(context: Context, srcPath: String, callBack: (File) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val newOpts = BitmapFactory.Options()
            //开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true
            var bitmap = BitmapFactory.decodeFile(srcPath, newOpts) //此时返回bm为空

            newOpts.inJustDecodeBounds = false
            val w = newOpts.outWidth
            val h = newOpts.outHeight
            //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
            val hh = 200f //这里设置高度为200f
            val ww = 200f //这里设置宽度为200f

            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            var be = 1 //be=1表示不缩放
            if (w > h && w > ww) { //如果宽度大的话根据宽度固定大小缩放
                be = (newOpts.outWidth / ww).toInt()
            } else if (w < h && h > hh) { //如果高度高的话根据宽度固定大小缩放
                be = (newOpts.outHeight / hh).toInt()
            } else {
                be = (newOpts.outWidth / ww).toInt()
            }
            if (be <= 0) be = 1
            newOpts.inSampleSize = be //设置缩放比例
            //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts)

            val baseSharePath = getShareTempFilePath(context)
            val file = File(baseSharePath, "${Date().time}.jpg")
            val bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
            bos.flush()
            bos.close()
            if (file.exists()) {
                callBack.invoke(file)
            } else {
                callBack.invoke(File(srcPath))
            }
        } catch (e: Exception) {
            callBack.invoke(File(srcPath))
        }
    }
}

/**
 * 获取分享图片临时目录
 */
fun getShareTempFilePath(context: Context): String {
    val path = context.getExternalFilesDir(null)?.absolutePath?.toString() + "/share/img/"
    val file = File(path)
    if (file.mkdirs()) {
        return path
    }
    return path
}

/**
 * 保存bitmap到相册
 */
fun saveBitmapToAlbum(bitmap: Bitmap, callBack: CommonCallBackListener) = runBlocking {
    async {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        bitmap.recycle()
        val path =
            "${Environment.getExternalStorageDirectory().absolutePath}/DCIM/amber/amber_${Date().time}.png"
        val fos = FileOutputStream(path)
        fos.write(bos.toByteArray())
        fos.flush()
        fos.close()
        bos.close()
        notifyPhotoAlbum(File(path))
    }
    callBack.invoke()
}

/**
 * 保存Bitmap并压缩
 */
fun saveBitmapFile(bitmap: Bitmap, cacheRoot: Boolean = false, callBack: (File) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
//        bitmap.recycle()
//        val parentFilePath = BaseApplication.getInstance().getExternalFilesDir(null)?.absolutePath?.toString() + "/poster/img/"
        val parentFilePath = if (cacheRoot) {
            zipImgPathDir(BaseApplication.getInstance().applicationContext, "/")
        } else {
            BaseApplication.getInstance()
                .getExternalFilesDir(null)?.absolutePath?.toString() + "/poster/img/"
        }
        File(parentFilePath).apply {
            if (!this.exists()) this.mkdirs()
        }
        val path = "${parentFilePath}poster_${Date().time}.jpg"
        val fos = FileOutputStream(path)
        fos.write(bos.toByteArray())
        fos.flush()
        fos.close()
        bos.close()
        withContext(Dispatchers.Main) {
            callBack.invoke(File(path))
        }
    }
}

/**
 * 下载完成后通知相册更新
 */
private fun notifyPhotoAlbum(file: File) = try {
    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    val uri = Uri.fromFile(file)
    intent.data = uri
    BaseApplication.getInstance().sendBroadcast(intent)
} catch (e: Exception) {
    e.printStackTrace()
}

/**
 * 加载图片
 *
 * @param url
 */
@SuppressLint("CheckResult")
fun ImageView.loadImage(
    url: String? = "",
    @DrawableRes errorId: Int = 0,
    @DrawableRes placeId: Int = 0,
    roundingRadius: Int = 0,
    @DrawableRes resource: Int = -1,
    borderSize: Int = 0,
    blurTransformation: Int = 0,
    @ColorInt borderColor: Int = 0,
    apply: RequestBuilder<Drawable>.() -> Unit = {}
) {
    if (null != context) {
        Glide.with(context).load(url.let {
            if (TextUtils.isEmpty(url)) resource else it
        }).apply {
            if (errorId != 0) {
                error(errorId)
            }
            if (placeId != 0) {
                placeholder(placeId)
            }

            if (roundingRadius != 0 && blurTransformation != 0) {
                transform(
                    CenterCrop(),
                    RoundedCorners(roundingRadius),
                    BlurTransformation(blurTransformation)
                )
            } else if (roundingRadius != 0) {
                transform(
                    CenterCrop(),
                    RoundedCorners(roundingRadius),
                )
            } else if (blurTransformation != 0) {
                transform(BlurTransformation(blurTransformation))
            }
            if (borderSize != 0) {
                transform(
                    CropCircleWithBorderTransformation(
                        borderSize, if (borderColor == 0) Color.WHITE else borderColor
                    )
                )
            }
            transition(DrawableTransitionOptions.withCrossFade())
            apply()
        }.into(this)
    }
}


/**
 * 图片预加载
 */
fun Context.preload(url: String, listener: RequestListener<Drawable>? = null) {
    Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.RESOURCE).listener(listener)
        .preload()
}


/**
 * 加载gif
 *
 * @param url       图片路径
 */
fun ImageView.loadAsGifImage(url: String) {
    Glide.with(context).asGif().load(url).listener(object : RequestListener<GifDrawable> {
        override fun onLoadFailed(
            e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: GifDrawable?,
            model: Any?,
            target: Target<GifDrawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            if (resource is GifDrawable) {
                resource.setLoopCount(1)
            }
            return false
        }
    }).into(this)
}

/**
 * 模糊图
 */
fun ImageView.loadBlurImage(url:String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Glide.with(context)
            .load(url)
            .into(this)
        val blur = RenderEffect.createBlurEffect(
            30f, 30f, Shader.TileMode.CLAMP
        )
        this.setRenderEffect(blur)
    }else{
        this.loadImage(
            url = url,
            blurTransformation = 30
        )
    }
}