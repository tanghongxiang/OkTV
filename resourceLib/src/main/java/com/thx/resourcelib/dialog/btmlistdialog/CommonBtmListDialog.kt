package com.thx.resourcelib.dialog.btmlistdialog

import android.content.Context
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.core.BottomPopupView
import com.thx.resourcelib.R
import com.thx.resourcelib.base.BaseApplication
import com.thx.resourcelib.databinding.DialogCommonBtmListBinding
import com.thx.resourcelib.ext.CommonCallBackListener
import com.thx.resourcelib.ext.getResourceString

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2022/7/26 11:53 上午
 */
class CommonBtmListDialog(context: Context, var title: String, var dataList: ArrayList<String>) :
    BottomPopupView(context) {

    private var mBinding: DialogCommonBtmListBinding? = null

    private var adapter: CommonBtmAdapter? = null

    private var cameraOption: CommonCallBackListener? = null

    private var albumOption: CommonCallBackListener? = null

    private var dismissForOutListener: CommonCallBackListener? = null

    private var isOptionDismiss = false

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_common_btm_list
    }

    override fun onCreate() {
        super.onCreate()
        mBinding = DialogCommonBtmListBinding.bind(popupImplView)
        adapter =
            CommonBtmAdapter(R.layout.dialog_item_commonbtm_layout, dataList, ::itemClickListener)
        mBinding?.mRecyclerView?.layoutManager = LinearLayoutManager(context)
        mBinding?.mRecyclerView?.adapter = adapter
        mBinding?.tvAlertTitle?.text = title
        mBinding?.tvAlertCancel?.text = getResourceString(
            BaseApplication.getInstance().applicationContext,
            R.string.dialog_cancel
        )
        mBinding?.tvAlertCancel?.setOnClickListener { dismiss() }
    }

    fun itemClickListener(pos: Int) {
        if (pos == 0) {
            cameraOption?.let { it() }
        } else {
            albumOption?.let { it() }
        }
        isOptionDismiss = true
        dismiss()
    }

    override fun onDismiss() {
        super.onDismiss()
        if (!isOptionDismiss) {
            dismissForOutListener?.let { lis -> lis() }
        }
        isOptionDismiss = false
    }

    fun visibleTitle(b: Boolean) {
        mBinding?.tvAlertTitle?.isVisible = b
    }

    fun registerListener(
        cameraOption: CommonCallBackListener,
        albumOption: CommonCallBackListener
    ) {
        this.cameraOption = cameraOption
        this.albumOption = albumOption
    }

    /**
     * 注册弹框外点击或者返回键关闭弹框导致弹框关闭监听
     */
    fun registerDismissForOutListener(dismissForOutListener: CommonCallBackListener) {
        this.dismissForOutListener = dismissForOutListener
    }

}