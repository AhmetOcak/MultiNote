package com.ahmetocak.multinote.utils.audio.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import javax.inject.Inject

class MNAudioPlayerImpl @Inject constructor() : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var isMediaEnded: Boolean = true

    override fun initializeMediaPlayer(onCompletion: () -> Unit) {
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )

            mediaPlayer!!.setOnCompletionListener {
                mediaPlayer!!.stop()
                isMediaEnded = true
                onCompletion()
            }
        } catch (e: Exception) {
            Log.e("MNAudioPlayerImpl", e.stackTraceToString())
        }
    }

    override fun play(audioUrl: String) {
        try {
            if (isMediaEnded) {
                mediaPlayer!!.apply {
                    stop()
                    reset()
                    setDataSource(audioUrl)
                    prepare()
                    start()
                }
                isMediaEnded = false
            } else {
                mediaPlayer!!.start()
            }
        } catch (e: Exception) {
            Log.e("MNAudioPlayerImpl", e.stackTraceToString())
        }
    }

    override fun pause() {
        try {
            mediaPlayer!!.pause()
        } catch (e: Exception) {
            Log.e("MNAudioPlayerImpl", e.stackTraceToString())
        }
    }

    override fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        isMediaEnded = true
    }

    override fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false
}