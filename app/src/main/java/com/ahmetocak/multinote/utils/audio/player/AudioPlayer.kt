package com.ahmetocak.multinote.utils.audio.player

interface AudioPlayer {
    fun initializeMediaPlayer(onCompletion: () -> Unit)
    fun play(audioUrl: String): Boolean
    fun pause()
    fun releaseMediaPlayer()
    fun isPlaying(): Boolean
}