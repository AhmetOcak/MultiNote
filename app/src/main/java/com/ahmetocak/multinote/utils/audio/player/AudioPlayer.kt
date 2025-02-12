package com.ahmetocak.multinote.utils.audio.player

interface AudioPlayer {
    fun initializeMediaPlayer(onCompletion: () -> Unit)
    fun play(audioUrl: String)
    fun stop()
    fun releaseMediaPlayer()
}