package com.thx.resourcelib.dialog

import android.content.Context
import com.lxj.xpopup.core.CenterPopupView
import com.thx.resourcelib.R
import com.thx.resourcelib.databinding.DialogRequestPermissionsAlertLayoutBinding
import com.thx.resourcelib.ext.CommonCallBackListener
import com.thx.resourcelib.ext.getResourceString

/**
 * @Description:申请权限之前的确认弹框
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2023/12/11 11:31 AM
 */
class RequestPermissionsDialog(context: Context) : CenterPopupView(context) {

    private var mBinding: DialogRequestPermissionsAlertLayoutBinding? = null

    private var onBtnSureClickListener: CommonCallBackListener? = null

    override fun getImplLayoutId() = R.layout.dialog_request_permissions_alert_layout

    override fun onCreate() {
        super.onCreate()
        mBinding = DialogRequestPermissionsAlertLayoutBinding.bind(popupImplView)
        mBinding?.tvContent?.text = getResourceString(
            context,
            R.string.request_permission_content_text
        )
        mBinding?.btnSure?.setOnClickListener {
            dismiss()
            onBtnSureClickListener?.invoke()
        }
    }

    /**
     * 确认按钮点击
     */
    fun registerOnBtnClickListener(onBtnSureClickListener: CommonCallBackListener) {
        this.onBtnSureClickListener = onBtnSureClickListener
    }


}