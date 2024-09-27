package com.thx.resourcelib.dialog.btmlistdialog

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.thx.resourcelib.R
import com.thx.resourcelib.ext.IntCallBackListener

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2022/7/26 12:32 下午
 */
class CommonBtmAdapter(
    layoutResId: Int,
    data: MutableList<String>?,
    var itemClickListener: IntCallBackListener
) :
    BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {

    override fun convert(holder: BaseViewHolder, item: String): Unit = holder.run {
        setText(R.id.tvAlert, item)
        getView<TextView>(R.id.tvAlert).setOnClickListener {
            itemClickListener(layoutPosition)
        }
    }

}