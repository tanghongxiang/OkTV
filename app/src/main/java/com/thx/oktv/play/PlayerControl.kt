package com.thx.oktv.play

import androidx.annotation.OptIn
import androidx.core.view.isVisible
import androidx.media3.common.util.UnstableApi

/**
 * 展示loading
 */
@OptIn(UnstableApi::class)
fun TvPlayerActivity.showLoading() {
    mBinding.loadingLayout.isVisible = true
}

/**
 * 隐藏loading
 */
@OptIn(UnstableApi::class)
fun TvPlayerActivity.hideLoading() {
    mBinding.loadingLayout.isVisible = false
}

/**
 * 显示网速&缓冲进度
 */
@OptIn(UnstableApi::class)
fun TvPlayerActivity.showNetSpeed(loadPercent: String, netSpeed: String) {
    mBinding.tvNetSpeed.text = "${loadPercent}\n${netSpeed}"
}