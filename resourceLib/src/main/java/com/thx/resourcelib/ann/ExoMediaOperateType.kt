package com.thx.resourcelib.ann

import androidx.annotation.IntDef

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/7/16 6:20 PM
 */
@IntDef(
    ExoMediaOperateType.TYPE_FOR_PLAY_DEFAULT,
    ExoMediaOperateType.TYPE_FOR_PLAY_PREVIOUS,
    ExoMediaOperateType.TYPE_FOR_PLAY_NEXT
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class ExoMediaOperateType {

    companion object {

        /** 默认-播放当前歌曲 */
        const val TYPE_FOR_PLAY_DEFAULT = 0

        /** 播放上一首 */
        const val TYPE_FOR_PLAY_PREVIOUS = 1

        /** 播放下一首 */
        const val TYPE_FOR_PLAY_NEXT = 2

    }

}