package com.thx.oktv.play

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.upstream.BandwidthMeter
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjq.toast.Toaster
import com.thx.oktv.databinding.AppActivityTvPlayerBinding
import com.thx.resourcelib.ARouterPath
import com.thx.resourcelib.base.BaseApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/9/26 下午6:23
 */
@UnstableApi
@Route(path = ARouterPath.TvPlayerActivity)
class TvPlayerActivity : ComponentActivity() {

    /** 播放器 */
    private var player: ExoPlayer? = null

    /** 网速 */
    private val bandwidthMeter =
        DefaultBandwidthMeter.Builder(BaseApplication.getInstance()).build()

    /** 当前网速 */
    private var currentNetSpeedStr = ""

    /** viewBinding */
    lateinit var mBinding: AppActivityTvPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = AppActivityTvPlayerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        // 网速变化
        bandwidthMeter.addEventListener(Handler(Looper.getMainLooper()), netSpeedChangeListener)
        player = ExoPlayer.Builder(this).setBandwidthMeter(bandwidthMeter).build()
        player?.setVideoTextureView(mBinding.textureView)
        player?.addListener(playerListener)
        play(intent?.extras?.getString("playUrl") ?: "")
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    /**
     * 播放器回调
     */
    private var playerListener = object : Player.Listener {

        /**
         * 播放状态更改
         */
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            Log.e("exo_media_play", "=====onIsPlayingChanged：$isPlaying")
        }

        /**
         * 播放状态回调
         */
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    // 缓冲中
                    showLoading()
                    lifecycleScope.launch(Dispatchers.IO) {
                        while (mBinding.loadingLayout.isVisible) {
                            withContext(Dispatchers.Main) {
                                showNetSpeed(
                                    "${player?.bufferedPercentage ?: 0}%",
                                    currentNetSpeedStr
                                )
                            }
                            delay(500)
                        }
                    }
                    Log.e(
                        "exo_media_play",
                        "===onPlaybackStateChanged:缓冲中${player?.bufferedPercentage}"
                    )
                }

                Player.STATE_READY -> {
                    // 缓冲结束
                    Log.e("exo_media_play", "===onPlaybackStateChanged:缓冲结束")
                    hideLoading()
                }

                Player.STATE_ENDED -> {
                    // 播放结束
                    Log.e("exo_media_play", "===onPlaybackStateChanged:播放结束")
                }
            }
        }

        /**
         * 音量大小发生改变
         */
        override fun onVolumeChanged(volume: Float) {
            Log.e("exo_media_play", "===onVolumeChanged:$volume")
        }

        /**
         * 播放错误
         */
        override fun onPlayerError(error: PlaybackException) {
            when (error.errorCode) {
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
                    // 网络错误
                    Toaster.show("网络异常~")
                }

                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
                    // 网络超时
                    Toaster.show("网络超时~")
                }

                PlaybackException.ERROR_CODE_BAD_VALUE -> {
                    // 地址错误
                    Toaster.show("播放地址错误~")
                }

                else -> {
                    // 其他错误
                    Toaster.show("播放错误~")
                }
            }
            hideLoading()
            Log.e("exo_media_play", "===onPlayerError：${error.errorCode}:${error.errorCodeName}")
        }

        /**
         * 当播放转换到媒体项或根据当前重复模式开始重复媒体项时调用。
         * 注意，当getCurrentTimeline()的值变为非空或空时，也会调用此回调。
         */
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            Log.e("exo_media_play", "===onMediaItemTransition：${mediaItem?.mediaId}:${reason}")
        }
    }

    /**
     * 网速变化
     */
    private var netSpeedChangeListener =
        BandwidthMeter.EventListener { elapsedMs, bytesTransferred, bitrateEstimate ->
            val res = bitrateEstimate / 1000f
            currentNetSpeedStr = if (res > 1024f) {
                "${(res / 1024f).toLong()}M/s"
            } else {
                "${res.toLong()}KB/s"
            }
            Log.e("exo_media_play", "===当前网速${currentNetSpeedStr}")
        }

    /**
     * 释放播放器
     */
    private fun release() {
        this.player?.pause()
        this.player?.removeListener(playerListener)
        this.player?.release()
        this.player = null
    }

    /**
     * 播放
     */
    @OptIn(UnstableApi::class)
    private fun play(url: String) {
        val error = player?.playerError
        if (error != null) {
            player?.prepare()
        }
//        // 创建 MediaItem，指定 m3u8 URL
//        val mediaItem = MediaItem.fromUri(Uri.parse("https://amd.art-amber.com/vod/202406/19/20240619073626531.mp4"))
//        // 设置 mediaItem 并准备播放器
//        player?.setMediaItem(mediaItem)
//        player?.prepare()
//        player?.playWhenReady = true
//        player?.play()

//        // 创建 MediaItem，指定 RTMP URL
//        val mediaItem = MediaItem.fromUri(Uri.parse("rtmp://ivi.bupt.edu.cn:1935/livetv/ahhd"))
//        // 创建 RTMP 数据源工厂
//        val rtmpDataSourceFactory = RtmpDataSource.Factory()
//        // 创建数据源
//        val mediaSource = DefaultMediaSourceFactory(rtmpDataSourceFactory)
//            .createMediaSource(mediaItem)

//        val videoItem = MediaItem.Builder().setUri(Uri.parse("rtmp://ivi.bupt.edu.cn:1935/livetv/cetv1")).build()
//        val videoSource = ProgressiveMediaSource.Factory(RtmpDataSource.Factory()).createMediaSource(videoItem)
//        // 为播放器设置 MediaSource
//        player?.setMediaSource(videoSource)
//        // 准备播放器并播放
//        player?.prepare()
//        player?.play()

        if (url.endsWith(".mp4")) {
            // 创建 MediaItem，指定 m3u8 URL
            val mediaItem =
                MediaItem.fromUri(Uri.parse("https://amd.art-amber.com/vod/202406/19/20240619073626531.mp4"))
            // 设置 mediaItem 并准备播放器
            player?.setMediaItem(mediaItem)
            player?.prepare()
            player?.playWhenReady = true
            player?.play()
        } else {
            val videoItem = MediaItem.Builder().setUri(Uri.parse(url)).build()
            val videoSource =
                HlsMediaSource.Factory(DefaultDataSource.Factory(this)).createMediaSource(videoItem)
            // 为播放器设置 MediaSource
            player?.setMediaSource(videoSource)
            // 准备播放器并播放
            player?.prepare()
            player?.play()
        }
    }

}

