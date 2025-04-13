package com.ahmetocak.multinote.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri

fun getVideoThumbnail(context: Context, uri: Uri): Bitmap? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val bitmap = retriever.getFrameAtTime(0)
        retriever.release()
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getAudioDuration(context: Context, uri: Uri): Pair<String, String> {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, uri)
        val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()
        val durationMs = durationStr?.toLongOrNull() ?: 0L
        val minutes = ((durationMs / 1000) / 60).toString()
        val seconds = ((durationMs / 1000) % 60).toString()
        return Pair(minutes, seconds)
    } catch (e: Exception) {
        e.printStackTrace()
        Pair("", "")
    }
}