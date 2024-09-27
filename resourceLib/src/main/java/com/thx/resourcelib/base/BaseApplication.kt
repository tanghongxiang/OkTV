package com.thx.resourcelib.base

import android.app.Activity
import android.content.Context
import android.util.Log
import android.webkit.WebView
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.hjq.toast.Toaster
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.tencent.mmkv.MMKV
import com.thx.commonlibrary.base.RouterFrameApplication
import com.thx.resourcelib.BuildConfig
import com.thx.resourcelib.R
import com.thx.resourcelib.widget.HomeRefreshHeaderLayout
import com.umeng.commonsdk.UMConfigure
import me.jessyan.autosize.AutoSizeConfig

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/24 4:24 PM
 */
open class BaseApplication : RouterFrameApplication() {

    private var nfcCardInfo: String = ""

    companion object {
        private var mInstance: BaseApplication? = null

        fun getInstance(): BaseApplication {
            if (mInstance != null) {
                return mInstance!!
            } else {
                throw IllegalArgumentException("Application is null")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        initAutoSize()
        // 这行代码是为了解决包含H5页面的页面国际化问题，切记不要删
        try {
            WebView(this).destroy()
        } catch (e: Exception) {
        }
        // MMKV初始化
        try {
            MMKV.initialize(this)
        } catch (e: UnsatisfiedLinkError) {
            Log.e("xiang", "======MMKV报错了")
        }
        // 预初始化友盟
        UMConfigure.preInit(this, "66cc289dcac2a664de9937a0", "OkTV_default")
    }

    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 初始化三方库
     */
    fun initThirdPart() {
        if (BuildConfig.DEBUG) {   // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.printStackTrace() // 打印日志的时候打印线程堆栈
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
        //刷新
        initSmartRefreshLayout()
        // 初始化 Toast 框架
        Toaster.init(this)
        // 正式初始化友盟
        UMConfigure.init(
            this,
            "66cc289dcac2a664de9937a0",
            "OkTV_default",
            UMConfigure.DEVICE_TYPE_PHONE,
            ""
        )
    }

    /**
     * 更换语言后重新recreate activity
     */
    fun recreateAllActivity() {
        Log.e("xiang", "===recreate页面个数：${getActivityStack().size}")
        if (getActivityStack().isEmpty()) return
        getActivityStack().forEach {
            it.recreate()
        }
    }

    /**
     * 保存NFC扫卡内容
     */
    fun saveNfcCardInfo(content: String) {
        this.nfcCardInfo = content
    }

    /**
     * 获取NFC扫卡内容
     */
    fun getNfcCardInfo(): String {
        return nfcCardInfo
    }


    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */


    /**
     * 今日头条适配
     */
    private fun initAutoSize() {
        AutoSizeConfig.getInstance().setBaseOnWidth(true)       //是否全局按照宽度进行等比例适配
//            .setUseDeviceSize(true)   //为使用设备的实际尺寸 (包含状态栏)
            .setExcludeFontScale(true)  //App 内的字体大小不随系统设置中字体大小而改变.
            .isCustomFragment = true
    }

    /**
     * Smart刷新
     */
    private fun initSmartRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { _: Context?, layout: RefreshLayout ->
            //全局设置主题颜色
            layout.setPrimaryColorsId(R.color.transparent, R.color.white)
            HomeRefreshHeaderLayout(applicationContext)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { _: Context, layout: RefreshLayout ->
            //全局设置主题颜色
            layout.setPrimaryColorsId(R.color.transparent, R.color.white)
            ClassicsFooter(applicationContext).setAccentColor(
                ContextCompat.getColor(
                    applicationContext, R.color.color_999999
                )
            )
        }
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "没有更多了~"
    }

    /* ======================================================= */
    /* Listener/监听                                            */
    /* ======================================================= */

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
        Log.e("xiang", "=========页面关闭：${activity.javaClass.simpleName}")
    }

}