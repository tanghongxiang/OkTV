package com.thx.resourcelib.utils.exo.music

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.media3.common.ForwardingPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.ListenableFuture
import com.thx.resourcelib.ann.ExoMediaOperateType
import com.thx.resourcelib.base.BaseApplication

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/7/19 下午5:23
 */
class AudioMediaSessionService : MediaSessionService() {

    /** 音频焦点Session */
    private var mediaSession: MediaSession? = null

    /** 播放器 */
    private var player: ExoPlayer? = null

    /** 获取音频焦点Lock变量 */
    private var focusLock: String = "AmberMusicPlayService"

    /** 音频管理类 */
    private var audioManager: AudioManager? = null

    /** 音频焦点请求类 */
    private var focusRequest: AudioFocusRequest? = null

    /** 音频焦点回调Handler */
    private var audioFocusCallBackHandler: Handler = Handler(Looper.getMainLooper())

    /** 是否播放中断 */
    private var playInterruption:Boolean = false

    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        player?.let {
            val forwardingPlayer = object : ForwardingPlayer(it) {
                override fun play() {
                    requestAudioFocus(true, ExoMediaOperateType.TYPE_FOR_PLAY_DEFAULT)
                }

                override fun seekToPrevious() {
//                    super.seekToPrevious()
                    requestAudioFocus(true, ExoMediaOperateType.TYPE_FOR_PLAY_PREVIOUS)
                }

                override fun seekToNext() {
//                    super.seekToNext()
                    requestAudioFocus(true, ExoMediaOperateType.TYPE_FOR_PLAY_NEXT)
                }
            }
            mediaSession = MediaSession.Builder(this, forwardingPlayer)
                .setCallback(mediaSessionCallBack)
                .build()
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    // The user dismissed the app from the recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player!!
        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED
        ) {
            // Stop the service if not playing, continue playing in the background
            // otherwise.
            stopSelf()
        }
    }

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    /**
     * MediaSession回调
     */
    private var mediaSessionCallBack = object : MediaSession.Callback {

        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            Log.e("exo_media_play", "====onConnect")
            return super.onConnect(session, controller)
        }

        /**
         * 控制器连接后立即调用。这是用于控制器的自定义初始化。
         * 注意，对控制器的调用(例如sendCustomCommand, setCustomLayout)在这里工作，
         * 但在onConnect中不起作用，因为控制器还没有在onConnect中连接。
         */
        override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
            super.onPostConnect(session, controller)
            Log.e("exo_media_play", "====onPostConnect")
        }

        override fun onDisconnected(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ) {
            super.onDisconnected(session, controller)
            Log.e("exo_media_play", "====onDisconnected")
        }

        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>
        ): ListenableFuture<MutableList<MediaItem>> {
            Log.e("exo_media_play", "====onAddMediaItems")
            return super.onAddMediaItems(mediaSession, controller, mediaItems)
        }

        override fun onCustomCommand(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle
        ): ListenableFuture<SessionResult> {
            Log.e("exo_media_play", "====onCustomCommand")
            return super.onCustomCommand(session, controller, customCommand, args)
        }

    }

    /**
     * 请求音频焦点
     */
    private fun requestAudioFocus(
        autoStart: Boolean = false,
        @ExoMediaOperateType operateType: Int
    ) {
        audioManager =
            BaseApplication.getInstance().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val playbackAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(playbackAttributes)
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(afChangeListener, audioFocusCallBackHandler)
            .build()
        val res = audioManager?.requestAudioFocus(focusRequest!!)
        synchronized(focusLock) {
            when (res) {
                AudioManager.AUDIOFOCUS_REQUEST_FAILED -> {
                    // 获取焦点失败
                }

                AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> run {
                    // 获取焦点成功
                    if (!autoStart) return@run
                    when (operateType) {
                        ExoMediaOperateType.TYPE_FOR_PLAY_PREVIOUS -> {
                            // 上一首
                            val totalCount = player?.mediaItemCount ?: 0
                            val currentPlayPos = player?.currentMediaItemIndex ?: 0
//                            if(true == player?.hasPreviousMediaItem()){
                            if (currentPlayPos > 0) {
//                                player?.seekToPrevious()
                                player?.seekToDefaultPosition(currentPlayPos - 1)
                            } else {
                                player?.seekToDefaultPosition(totalCount - 1)
                            }
                        }

                        ExoMediaOperateType.TYPE_FOR_PLAY_NEXT -> {
                            // 下一首
                            val totalCount = player?.mediaItemCount ?: 0
                            val currentPlayPos = player?.currentMediaItemIndex ?: 0
//                            if(true == player?.hasNextMediaItem()){
                            if (currentPlayPos < totalCount - 1) {
//                                player?.seekToNext()
                                player?.seekToDefaultPosition(currentPlayPos + 1)
                            } else {
                                player?.seekToDefaultPosition(0)
                            }
                        }

                        else -> {
                            player?.play()
                        }
                    }
                }

                AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> {
                    // 延时获取焦点
                }

                else -> {
                    // 其他状态
                }
            }
        }
    }

    /**
     * 音频焦点变化
     */
    private var afChangeListener = AudioManager.OnAudioFocusChangeListener {
        when (it) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                //当其他应用申请焦点之后又释放焦点会触发此回调
                //可重新播放音乐
                Log.d("exo_media_play", "AUDIOFOCUS_GAIN")
                if(playInterruption){
                    // 只有播放被打断才继续播放
                    // 重新请求焦点并播放
                    requestAudioFocus(true, ExoMediaOperateType.TYPE_FOR_PLAY_DEFAULT)
                }
            }

            AudioManager.AUDIOFOCUS_LOSS -> {
                //长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时，
                //会触发此回调事件，例如播放QQ音乐，网易云音乐等
                //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
                Log.d("exo_media_play", "AUDIOFOCUS_LOSS")
//                stop()
                //释放焦点，该方法可根据需要来决定是否调用
                //若焦点释放掉之后，将不会再自动获得
//                mAudioManager.abandonAudioFocus(mAudioFocusChange)
                if(true == player?.isPlaying){
                    this.playInterruption = true
                }
                player?.pause()
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，
                //会触发此回调事件，例如播放短视频，拨打电话等。
                //通常需要暂停音乐播放
//                stop()
                if(true == player?.isPlaying){
                    this.playInterruption = true
                }
                Log.d("exo_media_play", "AUDIOFOCUS_LOSS_TRANSIENT")
                player?.pause()
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                //短暂性丢失焦点并作降音处理
                if(true == player?.isPlaying){
                    this.playInterruption = true
                }
                Log.d("exo_media_play", "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
                player?.pause()
            }

        }
    }

}