package com.thx.resourcelib.base

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.ViewModelStore
import androidx.viewbinding.ViewBinding
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.Toaster
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.thx.commonlibrary.utils.DisplayUtils
import com.thx.resourcelib.ann.TypeRequestPermissionResult
import com.thx.resourcelib.dialog.LoadingView
import com.thx.resourcelib.dialog.RequestPermissionsDialog
import com.thx.resourcelib.ext.CommonCallBackListener
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.internal.CustomAdapt

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/25 1:37 PM
 */
abstract class BaseActivity<V : ViewBinding> : AppCompatActivity(), CustomAdapt {

    /** ViewModelStore */
    private var mViewModelStore: ViewModelStore? = null

    lateinit var mBinding: V

    /** loading View */
    var loadingView: LoadingView? = null

    /** 站内通知弹框 */
    private var mNotificationDialog: BasePopupView? = null

    /** viewBinding对象 */
    abstract fun viewBinding(): V

    /** 初始化View相关,return是否透明状态栏 */
    abstract fun initView(savedInstanceState: Bundle?): Boolean

    /** 初始化数据相关 */
    abstract fun initData(savedInstanceState: Bundle?)

    /** 是否重写onCreate()方法 */
    open fun overrideOnCreate(): Boolean = false

    /** 是否忽略状态栏设置(键盘顶起布局需求的时候可以重写此函数返回true) */
    open fun ignoreStatusSetting() = false

    /**
     * 设置透明状态栏时可以通过重写此函数来指定状态栏颜色灰浅
     * first:是否指定颜色
     * second:是否是深色
     */
    open fun statusBarColorForTransparent(): Pair<Boolean, Boolean> = Pair(false, false)

    /**
     * 如果需要手动设置状态栏的颜色，那么就需要重写这个函数,默认白色状态栏
     */
    open fun statusBarColor(): Int = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 屏幕常亮
        window.decorView.keepScreenOn = true
        mViewModelStore = ViewModelStore()
        if (loadingView == null) {
            loadingView = LoadingView.create(this)
        }
        viewBinding().apply {
            mBinding = this
            if (!overrideOnCreate()) {
                setContentView(this.root)
            }
        }
        if (ignoreStatusSetting()) {
            // 忽略状态栏的所有默认设置
            initView(savedInstanceState)
        } else {
            // 设置状态栏相关
            if (initView(savedInstanceState)) {
                // 透明状态栏
                var isBlack = false
                val statusColorMode = statusBarColorForTransparent()
                // 是否置顶状态栏文字颜色
                if (statusColorMode.first) {
                    // 置顶状态栏文字颜色
                    isBlack = statusColorMode.second
                }
                transparentStatusBar(isBlack)
            } else {
                // 实心状态栏
                setStatusBar(statusBarColor())
            }
        }
        initData(savedInstanceState)
    }

    override val viewModelStore: ViewModelStore
        get() = mViewModelStore ?: super.viewModelStore

    override fun onDestroy() {
        viewModelStore.clear()
        super.onDestroy()
    }

    /**
     * 透明状态栏
     * @param isBlack : 状态栏字体是否是深色
     */
    private fun transparentStatusBar(isBlack: Boolean) {
        val window = window
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
        window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                        or if (isBlack) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_VISIBLE
                )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        window.decorView.findViewById<FrameLayout>(android.R.id.content)
            .setPadding(0, 0, 0, 0)
    }

    /**
     * Android 6.0 以上设置状态栏颜色
     */
    private fun setStatusBar(@ColorInt color: Int) {
        // 设置状态栏底色颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = color
        window.navigationBarColor = Color.TRANSPARENT
        // 如果亮色，设置状态栏文字为黑色
        if (isLightColor(color)) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
//        if (!topPadding()) return
        window.decorView.findViewById<FrameLayout>(android.R.id.content)
            .setPadding(0, DisplayUtils.getStatusHeight(applicationContext), 0, 0)
    }

    /**
     * 判断颜色是不是亮色
     *
     * @param color
     * @return
     * @from
     */
    private fun isLightColor(@ColorInt color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) >= 0.5
    }

    /**
     * 申请权限之前弹出确认申请弹框
     */
    private fun showConfirmRequestPermissionDialog(callBack: CommonCallBackListener) {
        val pop = RequestPermissionsDialog(this)
        pop.registerOnBtnClickListener {
            callBack.invoke()
        }
        XPopup.Builder(this)
            .maxWidth(
                DisplayUtils.convertDp2Pixel(292f, applicationContext).toInt()
            )
            .asCustom(pop)
            .show()
    }

    /**
     * 申请权限操作
     */
    private fun requestPermissionsOperate(
        permissionsCallback: (@TypeRequestPermissionResult Int) -> Unit,
        vararg permissions: String
    ) {
        val mPermissions = XXPermissions.with(this)
//            .permission(permissions)
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
        mPermissions.permission(Permission.READ_MEDIA_IMAGES)
        mPermissions.permission(Permission.READ_MEDIA_VIDEO)
//        }else{
//            mPermissions.permission(Permission.WRITE_EXTERNAL_STORAGE)
//            mPermissions.permission(Permission.READ_EXTERNAL_STORAGE)
//        }
        if (!permissions.contains(Permission.WRITE_EXTERNAL_STORAGE)) {
            mPermissions.permission(Permission.WRITE_EXTERNAL_STORAGE)
        }
        permissions.forEach {
            mPermissions.permission(it)
        }
        mPermissions.request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                if (allGranted) {
                    // 所有权限都获取成功了
                    permissionsCallback.invoke(TypeRequestPermissionResult.TYPE_FOR_ALL_GRANTED)
                } else {
                    // 部分权限未获取成功
                    permissionsCallback.invoke(TypeRequestPermissionResult.TYPE_FOR_SOME_GRANTED)
                }
            }

            override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                if (doNotAskAgain) {
                    Toaster.show("相关权限被永久拒绝授权，请手动设置授予")
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(
                        BaseApplication.getInstance(),
                        permissions
                    )
                } else {
                    Toaster.show("相关权限获取失败")
                }
                permissionsCallback.invoke(TypeRequestPermissionResult.TYPE_FOR_DENIED)
            }
        })
    }


    /* ======================================================= */
    /* 屏幕适配相关                                              */
    /* ======================================================= */

    /** 屏幕宽 */
    private var mScreenWidth = 0f

    /** 屏幕高 */
    private var mScreenHeight = 0f

    private val FONT_NOT_SCALE = 1f


    override fun isBaseOnWidth() = true

    override fun getSizeInDp(): Float {
        if (mScreenWidth == 0f) return 375f
        val percent =
            if (mScreenWidth > mScreenHeight) mScreenWidth / mScreenHeight else mScreenHeight / mScreenWidth
        if (percent < 5 / 4f) {
            // 偏方形手机
            return 375f * 1.5f
        }
        return 375f
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.fontScale !== FONT_NOT_SCALE) {
            resources
        }
    }

    override fun getResources(): Resources? {
        if (applicationContext != null && mScreenWidth == 0f) {
            mScreenWidth = DisplayUtils.getScreenWidth(applicationContext) * 1f
            mScreenHeight = DisplayUtils.getScreenHeight(applicationContext) * 1f
        }
        val widthDP = sizeInDp
        if (widthDP == 375f) {
            val resources = super.getResources()
            if (resources != null && resources.configuration.fontScale !== 1.0f) {
                val configuration = resources.configuration
                configuration.fontScale = 1.0f
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
            return resources
        }
        val res = super.getResources() ?: return null
        //规避可能的空指针异常
        //强制字体大小不随系统改变而改变
        if (res.configuration.fontScale !== FONT_NOT_SCALE) {
            val newConfig = Configuration()
            newConfig.setToDefaults()
            res.updateConfiguration(newConfig, res.displayMetrics)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                createConfigurationContext(newConfig)
            } else {
                res.updateConfiguration(newConfig, res.displayMetrics)
            }
        }
        //用于解决本来适配正常的布局突然出现适配失效、适配异常等问题
        if (Looper.myLooper() == Looper.getMainLooper() && widthDP != 375f) {
            AutoSizeCompat.autoConvertDensity(res, widthDP, isBaseOnWidth)
        }
        return res
    }


    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 动态设置是否透明状态栏
     */
    fun changeStatusBarStyle(
        isTranslate: Boolean,
        @ColorInt statusBarColor: Int = Color.WHITE,
        blackStatusBarText: Boolean = true
    ) {
        if (isTranslate) {
            // 透明状态栏
            transparentStatusBar(blackStatusBarText)
        } else {
            // 实心状态栏
            setStatusBar(statusBarColor)
        }
    }

    /**
     * 高危权限申请
     * simple:Manifest.permission.CAMERA
     */
    fun requestPermissions(
        vararg permissions: String,
        permissionsCallback: (@TypeRequestPermissionResult Int) -> Unit
    ) {
        // 检查申请的全是是否都有
        val needRequest = !XXPermissions.isGranted(this, permissions)
        if (needRequest) {
            // 如果需要请求新的权限，需要在请求权限之前弹框告知(应用商店要求)
            showConfirmRequestPermissionDialog {
                requestPermissionsOperate(permissionsCallback, *permissions)
            }
        } else {
            requestPermissionsOperate(permissionsCallback, *permissions)
        }
    }

    /**
     * 弹出loading
     */
    fun showLoadingView(outCancel: Boolean = false) {
        loadingView?.setCanceledOnTouchOutside(outCancel)
        loadingView?.show()
    }

    /**
     * 关闭loading
     */
    fun hideLoadingView() {
        loadingView?.dismiss()
    }

}