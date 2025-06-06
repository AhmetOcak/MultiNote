package com.ahmetocak.multinote.utils.audio.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import javax.inject.Inject

class MNAudioPlayerImpl @Inject constructor() : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var isMediaEnded: Boolean = true
    private var currentAudio: String? = null

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

    override fun play(audioUrl: String): Boolean {
        if (currentAudio == null)
            currentAudio = audioUrl
        val returnVal = audioUrl == currentAudio
        try {
            if (audioUrl != currentAudio) {
                mediaPlayer!!.apply {
                    stop()
                    reset()
                    setDataSource(audioUrl)
                    prepare()
                    start()
                }
                isMediaEnded = false
            } else {
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
            }
            currentAudio = audioUrl
            return returnVal
        } catch (e: Exception) {
            Log.e("MNAudioPlayerImpl", e.stackTraceToString())
            return returnVal
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