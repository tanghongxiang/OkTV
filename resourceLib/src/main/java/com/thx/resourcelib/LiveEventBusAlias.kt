package com.thx.resourcelib

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/6/25 5:58 PM
 */
class LiveEventBusAlias {

    /**
     * 首页pass列表页
     */
    object HomeTabPassFragment{
        /** 列表Item点击 */
        const val DRAG_PASS_FOR_RESORT = "home_tab_pass_fragment_drag_pass_for_resort"
        /** 通知父Fragment当前是否拥有PASS卡 */
        const val NOTIFY_PARENT_HAS_PASS_COUNT = "notify_parent_has_pass_count"
        /** 通知父Fragment刷新求赠未读数 */
        const val NOTIFY_PARENT_TO_UPDATE_UNREAD_COUNT = "notify_parent_to_update_unread_count"
    }

}