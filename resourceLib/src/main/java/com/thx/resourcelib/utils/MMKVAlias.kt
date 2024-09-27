package com.thx.resourcelib.utils

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/25 3:43 PM
 */
class MMKVAlias {

    companion object{

        /** 当前APP是否是简版 */
        const val CURRENT_APP_VERSION_STYLE = "current_app_version_style"

        /** 用户登录信息 */
        const val USERLOGININFO = "user_login_info_data"

        /** 是否是打开过APP */
        const val IS_OPENED_APP = "common_is_opened_app"

        /** 隐私保护弹框是否已经弹出过 */
        const val PRIVACY_PROTECTION_SHOW_INFO = "privacy_protection_show_info"

        /** 当前APP选择的语言-Language */
        const val APP_CHANGE_LANGUAGE_FOR_LANGUAGE = "app_change_language_for_language"

        /** 当前APP选择的语言-AREA */
        const val APP_CHANGE_LANGUAGE_FOR_AREA = "app_change_language_for_area"

        /** 保存编辑过的分享内容 */
        const val SAVE_SHARE_INFO_CONTENT = "save_edit_share_content"

        /** 极光推送通知点击过来的参数 */
        const val J_PUSH_CLICK_INFO = "jpush_click_info"

        /** 是否提示过首页可以拖动排序 */
        const val IS_SHOW_PASS_LIST_CAN_DRAG = "is_show_pass_list_can_drag"

        /** 音乐播放器-上次播放专辑ID */
        const val MUSIC_PLAYER_HISTORY_ALBUM_ID = "music_player_history_album_id"

        /** 音乐播放器-上次播放歌曲ID */
        const val MUSIC_PLAYER_HISTORY_MUSIC_ID = "music_player_history_music_id"

        /** 音乐播放器-上次歌曲的播放进度 */
        const val MUSIC_PLAYER_HISTORY_MUSIC_PLAY_PROGRESS = "music_player_history_music_play_progress"

        /** PASS列表-记录上次的展示模式（大卡&小卡） */
        const val HOME_TAB_PASS_LIST_SHOW_MODE = "home_tab_pass_list_show_mode"


        val mInstance: MMKVAlias by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MMKVAlias()
        }

    }

}