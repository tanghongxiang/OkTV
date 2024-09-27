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
 * @Create Date: 2024/5/11 2:40 PM
 */
@IntDef({TypeRequestPermissionResult.TYPE_FOR_ALL_GRANTED, TypeRequestPermissionResult.TYPE_FOR_SOME_GRANTED, TypeRequestPermissionResult.TYPE_FOR_DENIED})
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD,ElementType.TYPE_USE})
public @interface TypeRequestPermissionResult {

    /**
     * 所有申请的权限都获取到了
     */
    int TYPE_FOR_ALL_GRANTED = 0;

    /**
     * 部分权限未获取到
     */
    int TYPE_FOR_SOME_GRANTED = 1;

    /**
     * 至少有一个权限被永久拒绝
     */
    int TYPE_FOR_DENIED = 2;

}
