package com.thx.resourcelib.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
import androidx.viewbinding.ViewBinding
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.toast.Toaster
import com.lxj.xpopup.XPopup
import com.thx.commonlibrary.utils.DisplayUtils
import com.thx.resourcelib.ann.TypeRequestPermissionResult
import com.thx.resourcelib.dialog.LoadingView
import com.thx.resourcelib.dialog.RequestPermissionsDialog
import com.thx.resourcelib.ext.CommonCallBackListener
import me.jessyan.autosize.internal.CustomAdapt

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/5/16 2:39 PM
 */
abstract class BaseFragment<V> : Fragment(), CustomAdapt where V : ViewBinding {

    /** ViewModelStore */
    private var mViewModelStore: ViewModelStore?=null

    lateinit var mBinding: V

    /** loading View */
    var loadingView: LoadingView? = null

    /** viewBinding对象 */
    abstract fun viewBinding(): V

    /** 初始化View相关,return是否透明状态栏 */
    abstract fun initView(savedInstanceState: Bundle?)

    /** 初始化数据相关 */
    abstract fun initData(savedInstanceState: Bundle?)

    open fun onNewIntent(intent: Intent?){}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = viewBinding()
        mViewModelStore = ViewModelStore()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        initData(savedInstanceState)
        if (loadingView == null) {
            loadingView = LoadingView.create(requireActivity())
        }
    }

    override val viewModelStore: ViewModelStore
        get() = mViewModelStore?:super.viewModelStore

    override fun onDestroyView() {
        viewModelStore.clear()
        super.onDestroyView()
    }

    /**
     * 申请权限之前弹出确认申请弹框
     */
    private fun Activity.showConfirmRequestPermissionDialog(callBack: CommonCallBackListener) {
        val pop = RequestPermissionsDialog(this)
        pop.registerOnBtnClickListener {
            callBack.invoke()
        }
        XPopup.Builder(this)
            .isViewMode(true)
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
        XXPermissions.with(this)
            .permission(permissions)
            .request(object : OnPermissionCallback {
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


    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 高危权限申请
     * simple:Manifest.permission.CAMERA
     */
    fun requestPermissions(
        activity: Activity,
        vararg permissions: String,
        permissionsCallback: (@TypeRequestPermissionResult Int) -> Unit
    ) {
        // 检查申请的全是是否都有
        val needRequest = !XXPermissions.isGranted(activity, permissions)
        if (needRequest) {
            // 如果需要请求新的权限，需要在请求权限之前弹框告知(应用商店要求)
            activity.showConfirmRequestPermissionDialog {
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