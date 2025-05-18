package com.ahmetocak.multinote.utils.audio.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import javax.inject.Inject

class MNAudioPlayerImpl @Inject constructor() : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null

    override fun initializeMediaPlayer(onCompletion: () -> Unit) {
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )

            mediaPlayer!!.setOnCompletionListener {
                stop()
                onCompletion()
            }
        } catch (e: Exception) {
            Log.e("MNAudioPlayerImpl", e.stackTraceToString())
        }
    }

    override fun play(audioUrl: String) {
        try {
            mediaPlayer!!.apply {
                stop()
                reset()
                setDataSource(audioUrl)
                prepare()
                start()
            }
        } catch (e: Exception) {
            Log.e("MNAudioPlayerImpl", e.stackTraceToString())
        }
    }

    override fun stop() {
        try {
            mediaPlayer!!.stop()
        } catch (e: Exception) {
            Log.e("MNAudioPlayerImpl", e.stackTraceToString())
        }
    }

    override fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false
}