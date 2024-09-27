package com.thx.resourcelib.ann;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2022/11/1 11:46 上午
 */
@IntDef({OpenAlbumType.TYPE_IMAGE, OpenAlbumType.TYPE_VIDEO, OpenAlbumType.TYPE_ALL, OpenAlbumType.TYPE_AUDIO})
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface OpenAlbumType {

    /**
     * 图片类型
     */
    int TYPE_IMAGE = 0;

    /**
     * 视频类型
     */
    int TYPE_VIDEO = 1;

    /**
     * 所有类型
     */
    int TYPE_ALL = 2;

    /**
     * 音频类型
     */
    int TYPE_AUDIO = 3;


}
