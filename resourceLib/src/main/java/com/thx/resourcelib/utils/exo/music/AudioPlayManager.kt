package com.thx.resourcelib.utils.exo.music

import android.content.ComponentName
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.thx.resourcelib.ann.ExoMediaOperateType
import com.thx.resourcelib.ann.ExoMediaPlayerStatus
import com.thx.resourcelib.base.BaseApplication
import com.thx.resourcelib.utils.MMKVAlias
import com.thx.resourcelib.utils.MMKVUtils
import com.thx.resourcelib.utils.exo.IMediaPlayCallBack
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.lang.reflect.InvocationTargetException

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/7/22 下午1:29
 */
class AudioPlayManager : Player.Listener {

    /** 当前注册的播放器回调列表 */
    private var registerCallBack: ArrayList<IMediaPlayCallBack> = arrayListOf()

    /** 播放器 */
    private var mMediaController: MediaController? = null

    /** lifecycleScope */
    private var lifecycleScope: WeakReference<LifecycleCoroutineScope>? = null

    /** 歌曲列表 */
    private var sourceList: ArrayList<MediaItem> = arrayListOf()

    companion object {
        val mInstance: AudioPlayManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AudioPlayManager()
        }
    }

    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * 初始化MediaController
     */
    private fun initMediaController() {
        registerCallBack.clear()
        val sessionToken = SessionToken(
            BaseApplication.getInstance().applicationContext,
            ComponentName(
                BaseApplication.getInstance().applicationContext,
                AudioMediaSessionService::class.java
            )
        )
        val controllerFuture =
            MediaController.Builder(
                BaseApplication.getInstance().applicationContext,
                sessionToken
            ).buildAsync()
        controllerFuture.addListener({
            // 播放器初始化完成
            controllerFuture.get()?.let {
                initController(it)
            }
        }, MoreExecutors.directExecutor())
    }

    /**
     * 注册播放器相关回调
     */
    private fun initController(controller: MediaController) {
        this.mMediaController = controller
        controller.addListener(this)
        startTimer()
    }

    /**
     * 定时获取当前播放器播放时间
     */
    private fun startTimer() {
        lifecycleScope?.get()?.launch {
            flow {
                while (true) {
                    emit(mMediaController?.currentPosition ?: 0L)
                    delay(1000)
                }
            }.collectLatest { res ->
                addPlayMusicProgressHistory(res)
                registerCallBack.forEach { itemCallBack ->
                    itemCallBack.onVideoPlayProgressChange(
                        res,
                        mMediaController?.duration ?: 0L
                    )
                }
            }
        }
    }

    /**
     * 请求音频焦点
     */
    private fun requestAudioFocus(
        autoStart: Boolean = false,
        @ExoMediaOperateType operateType: Int,
        defaultPlayPos: Int? = null
    ) {
        when (operateType) {
            ExoMediaOperateType.TYPE_FOR_PLAY_PREVIOUS -> {
                // 上一首
                val totalCount = mMediaController?.mediaItemCount ?: 0
                if (true == mMediaController?.hasPreviousMediaItem()) {
                    mMediaController?.seekToPrevious()
                } else {
                    mMediaController?.seekToDefaultPosition(totalCount - 1)
                }
            }

            ExoMediaOperateType.TYPE_FOR_PLAY_NEXT -> {
                // 下一首
                if (true == mMediaController?.hasNextMediaItem()) {
                    mMediaController?.seekToNext()
                } else {
                    mMediaController?.seekToDefaultPosition(0)
                }
            }

            else -> {
                if (defaultPlayPos != null) {
                    mMediaController?.seekToDefaultPosition(defaultPlayPos)
                }
                mMediaController?.isConnected
                mMediaController?.play()
            }
        }
    }

    /**
     * 记录播放列表播放历史-专辑ID
     */
    private fun addPlayAlbumIdHistory(sourceList: ArrayList<MediaItem>) {
        if (sourceList.size < 1) return
        val albumId = sourceList[0].mediaMetadata.extras?.getString("albumId") ?: return
        MMKVUtils.mInstance.encode(MMKVAlias.MUSIC_PLAYER_HISTORY_ALBUM_ID, albumId)
    }

    /**
     * 记录播放列表播放历史-歌曲ID
     */
    private fun addPlayMusicIdHistory(musicId: String) {
        if (TextUtils.isEmpty(musicId)) return
        MMKVUtils.mInstance.encode(MMKVAlias.MUSIC_PLAYER_HISTORY_MUSIC_ID, musicId)
    }

    /**
     * 记录播放列表播放历史-歌曲播放进度
     */
    private fun addPlayMusicProgressHistory(progress: Long) {
        if (sourceList.size == 0 || progress == 0L) return
        MMKVUtils.mInstance.encode(MMKVAlias.MUSIC_PLAYER_HISTORY_MUSIC_PLAY_PROGRESS, progress)
    }

    /* ======================================================= */
    /* Listener/监听                                            */
    /* ======================================================= */

    /**
     * 播放状态更改
     */
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        registerCallBack.forEach { itemCallBack ->
            itemCallBack.onVidePlayStatusChange(if (isPlaying) ExoMediaPlayerStatus.TYPE_FOR_PLAYING else ExoMediaPlayerStatus.TYPE_FOR_PAUSE)
        }
        Log.e("exo_media_play", "=====onIsPlayingChanged：$isPlaying")
    }

    /**
     * 播放状态回调
     */
    override fun onPlaybackStateChanged(playbackState: Int) {
        registerCallBack.forEach { itemCallBack ->
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    // 缓冲中
                    itemCallBack.onLoadingStatusChange(true)
                    Log.e("exo_media_play", "===onPlaybackStateChanged:缓冲中")
                }

                Player.STATE_READY -> {
                    // 缓冲结束
                    itemCallBack.onLoadingStatusChange(false)
                    Log.e("exo_media_play", "===onPlaybackStateChanged:缓冲结束")
                }

                Player.STATE_ENDED -> {
                    // 播放结束
                    itemCallBack.onVideoPlayComplete()
                    Log.e("exo_media_play", "===onPlaybackStateChanged:播放结束")
                }
            }
        }
    }

    /**
     * 音量大小发生改变
     */
    override fun onVolumeChanged(volume: Float) {
        registerCallBack.forEach { itemCallBack ->
            itemCallBack.onVolumeChanged(volume)
        }
        Log.e("exo_media_play", "===onVolumeChanged:$volume")
    }

    /**
     * 播放错误
     */
    override fun onPlayerError(error: PlaybackException) {
        registerCallBack.forEach { itemCallBack ->
            when (error.errorCode) {
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
                    // 网络错误
                    itemCallBack.onVidePlayStatusChange(ExoMediaPlayerStatus.TYPE_FOR_NETWORK_ERR)
                }

                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
                    // 网络超时
                    itemCallBack.onVidePlayStatusChange(ExoMediaPlayerStatus.TYPE_FOR_NETWORK_TIMEOUT)
                }

                PlaybackException.ERROR_CODE_BAD_VALUE -> {
                    // 地址错误
                    itemCallBack.onVidePlayStatusChange(ExoMediaPlayerStatus.TYPE_FOR_BAD_VALUE)
                }

                else -> {
                    // 其他错误
                    itemCallBack.onVidePlayStatusChange(ExoMediaPlayerStatus.TYPE_FOR_ERROR)
                }
            }
        }
        Log.e("exo_media_play", "===onPlayerError：${error.errorCode}:${error.errorCodeName}")
    }

    /**
     * 当播放转换到媒体项或根据当前重复模式开始重复媒体项时调用。
     * 注意，当getCurrentTimeline()的值变为非空或空时，也会调用此回调。
     */
    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        addPlayMusicIdHistory(mediaItem?.mediaId ?: "")
        registerCallBack.forEach { itemCallBack ->
            itemCallBack.onMediaItemChange(mediaItem, reason)
        }
        Log.e("exo_media_play", "===onMediaItemTransition：${mediaItem?.mediaId}:${reason}")
    }


    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 绑定lifecycleScope
     */
    fun bind(lifecycleScope: LifecycleCoroutineScope) {
        this.lifecycleScope = WeakReference<LifecycleCoroutineScope>(lifecycleScope)
        initMediaController()
    }

    /**
     * 释放播放器
     */
    fun release() {
        this.sourceList.clear()
        clearMusicList()
        this.registerCallBack.clear()
        this.mMediaController?.pause()
        this.mMediaController?.removeListener(this)
        this.lifecycleScope = WeakReference(null)
        this.mMediaController?.release()
    }

    /**
     * 注册页面播放回调
     */
    fun registerMusicPlayCallback(callBack: IMediaPlayCallBack) {
        registerCallBack.add(callBack)
    }

    /**
     * 取消页面播放回调
     */
    fun unRegisterMusicPlayCallback(callBack: IMediaPlayCallBack) {
        registerCallBack.remove(callBack)
    }

    /**
     * 添加歌曲列表
     * val mediaItem =
     *     MediaItem.Builder()
     *         .setMediaId("第一首歌")
     *         .setUri(Uri.parse("https://amd.art-amber.com/audio/202309/01/20230901173612280.mp3"))
     *         .setMediaMetadata(
     *             MediaMetadata.Builder()
     *                 .setArtist("David Bowie")
     *                 .setTitle("第一首歌曲啊")
     *                 .setArtworkUri(Uri.parse("https://img1.baidu.com/it/u=1045659411,1710051103&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=501"))
     *                 .build()
     *         )
     *         .build()
     */
    fun addMusicList(sourceList: ArrayList<MediaItem>) {
        // 记录播放器播放历史
        addPlayAlbumIdHistory(sourceList)

        this.sourceList = sourceList
//        val count = mMediaController?.mediaItemCount ?: 0
//        if (count > 0) {
//            mMediaController?.removeMediaItems(0, count)
//        }
        if (null == lifecycleScope?.get()) {
            throw InvocationTargetException(Throwable("请先调用bind()函数绑定生命周期！"))
        }
        mMediaController?.addMediaItems(sourceList)
        mMediaController?.playWhenReady = false
        mMediaController?.prepare()
    }

    /**
     * 清除播放列表
     */
    fun clearMusicList() {
        val count = mMediaController?.mediaItemCount ?: 0
        if (count > 0) {
            mMediaController?.removeMediaItems(0, count)
        }
    }

    /**
     * 设置播放器播放进度
     */
    fun seekTo(value: Long) {
        mMediaController?.seekTo(value)
    }

    /**
     * 设置播放器指定歌曲指定位置
     */
    fun seekTo(musicIndex: Int, pos: Long) {
        mMediaController?.seekTo(musicIndex, pos)
    }

    /**
     * 开始播放
     */
    fun play(pos: Int? = null) {
        val error = mMediaController?.playerError
        if (error != null) {
            val pos = mMediaController?.currentPosition ?: 0L
            mMediaController?.prepare()
            seekTo(pos)
        }
        requestAudioFocus(true, ExoMediaOperateType.TYPE_FOR_PLAY_DEFAULT, pos)
    }

    /**
     * 播放上一首
     */
    fun playPrevious() {
        val error = mMediaController?.playerError
        if (error != null) {
            val pos = mMediaController?.currentPosition ?: 0L
            mMediaController?.prepare()
            seekTo(pos)
        }
        requestAudioFocus(true, ExoMediaOperateType.TYPE_FOR_PLAY_PREVIOUS)
    }

    /**
     * 播放下一首
     */
    fun playNext() {
        val error = mMediaController?.playerError
        if (error != null) {
            val pos = mMediaController?.currentPosition ?: 0L
            mMediaController?.prepare()
            seekTo(pos)
        }
        requestAudioFocus(true, ExoMediaOperateType.TYPE_FOR_PLAY_NEXT)
    }

    /**
     * 暂停
     */
    fun pause() {
        mMediaController?.pause()
    }

    /**
     * 循环播放
     */
    fun looper(l: Boolean) {
        if (l) {
            mMediaController?.repeatMode = Player.REPEAT_MODE_ALL
        } else {
            mMediaController?.repeatMode = Player.REPEAT_MODE_OFF
        }
    }

    /**
     * 当前是否正在播放歌曲
     */
    fun isPlaying(): Boolean {
        return mMediaController?.isPlaying ?: false
    }

    /**
     * 当前播放器是否正在loading
     */
    fun isLoading(): Boolean {
        return mMediaController?.isLoading ?: false
    }

    /**
     * 当前播放器播放列表
     */
    fun playList(): ArrayList<MediaItem> {
        return sourceList
    }

    /**
     * 当前播放的歌曲Index
     */
    fun currentPlayIndex(): Int {
        return mMediaController?.currentMediaItemIndex ?: 0
    }

    /**
     * 当前播放的歌曲的信息
     */
    fun currentPlayItemInfo(): MediaItem? {
        return mMediaController?.currentMediaItem
    }

    /**
     * 获取当前进度
     */
    fun currentPlayProgress(): Pair<Long, Long> {
        return Pair(
            mMediaController?.currentPosition ?: 0,
            mMediaController?.duration ?: 0
        )
    }

}