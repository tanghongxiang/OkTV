package com.thx.resourcelib.utils.exo

import androidx.media3.common.MediaItem
import com.thx.resourcelib.ann.ExoMediaPlayerStatus

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/7/22 下午1:38
 */
interface IMediaPlayCallBack {

    /** 视频状态变化 */
    fun onVidePlayStatusChange(@ExoMediaPlayerStatus status: Int)

    /** 视频是否正在loading */
    fun onLoadingStatusChange(loading: Boolean)

    /** 视频播放结束了 */
    fun onVideoPlayComplete()

    /** 音量大小发生变化(0~1) */
    fun onVolumeChanged(value: Float)

    /** 播放进度回调 */
    fun onVideoPlayProgressChange(current: Long, total: Long)

    /** 资源切换 */
    fun onMediaItemChange(mediaItem: MediaItem?, reason: Int)

}