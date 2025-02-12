package com.ahmetocak.multinote.utils.audio.recorder

import java.io.File

interface AudioRecorder {
    fun startRecording(noteId: Int)
    fun stopRecording(): File
}