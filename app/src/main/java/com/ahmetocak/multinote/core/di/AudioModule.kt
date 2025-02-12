package com.ahmetocak.multinote.core.di

import android.content.Context
import com.ahmetocak.multinote.utils.audio.player.AudioPlayer
import com.ahmetocak.multinote.utils.audio.player.MNAudioPlayerImpl
import com.ahmetocak.multinote.utils.audio.recorder.AudioRecorder
import com.ahmetocak.multinote.utils.audio.recorder.MNAudioRecorderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioModule {

    @Singleton
    @Provides
    fun provideAudioPlayer(): AudioPlayer {
        return MNAudioPlayerImpl()
    }

    @Singleton
    @Provides
    fun provideAudioRecorder(@ApplicationContext context: Context): AudioRecorder {
        return MNAudioRecorderImpl(context)
    }
}