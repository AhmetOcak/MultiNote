package com.ahmetocak.multinote.utils.audio.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject
import java.io.FileOutputStream

class MNAudioRecorderImpl @Inject constructor(
    private val context: Context
) : AudioRecorder {

    private var recorder: MediaRecorder? = null
    private lateinit var audioFile: File

    private fun createMediaRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun startRecording(noteId: Int) {
        audioFile = File(context.cacheDir, "$noteId${LocalDateTime.now()}.mp3")

        createMediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(audioFile).fd)

            prepare()
            start()

            recorder = this
        }
    }

    override fun stopRecording(): File {
        recorder?.stop()
        recorder?.reset()
        recorder = null
        return audioFile
    }
}