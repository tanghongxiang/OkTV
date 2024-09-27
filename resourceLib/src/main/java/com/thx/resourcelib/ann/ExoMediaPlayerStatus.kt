package com.thx.resourcelib.ann

import androidx.annotation.IntDef

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/7/16 6:20 PM
 */
@IntDef(
    ExoMediaPlayerStatus.TYPE_FOR_PAUSE,
    ExoMediaPlayerStatus.TYPE_FOR_PLAYING,
    ExoMediaPlayerStatus.TYPE_FOR_ERROR,
    ExoMediaPlayerStatus.TYPE_FOR_NETWORK_ERR,
    ExoMediaPlayerStatus.TYPE_FOR_NETWORK_TIMEOUT,
    ExoMediaPlayerStatus.TYPE_FOR_BAD_VALUE
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class ExoMediaPlayerStatus {

    companion object {

        /** 播放暂停 */
        const val TYPE_FOR_PAUSE = 0

        /** 播放中 */
        const val TYPE_FOR_PLAYING = 1

        /** 播放错误 */
        const val TYPE_FOR_ERROR = 2

        /** 网络异常 */
        const val TYPE_FOR_NETWORK_ERR = 3

        /** 网络超时 */
        const val TYPE_FOR_NETWORK_TIMEOUT = 4

        /** 地址错误 */
        const val TYPE_FOR_BAD_VALUE = 5

    }

}