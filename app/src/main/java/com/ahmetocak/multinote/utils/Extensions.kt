package com.ahmetocak.multinote.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalDensity
import com.ahmetocak.multinote.model.NoteTag
import com.ahmetocak.multinote.model.NoteType
import java.io.File

@Composable
fun LazyStaggeredGridState.isScrollingUp(): State<Boolean> {
    return produceState(initialValue = true) {
        var lastIndex = 0
        var lastScroll = Int.MAX_VALUE
        snapshotFlow {
            firstVisibleItemIndex to firstVisibleItemScrollOffset
        }.collect { (currentIndex, currentScroll) ->
            if (currentIndex != lastIndex || currentScroll != lastScroll) {
                value = currentIndex < lastIndex ||
                        (currentIndex == lastIndex && currentScroll < lastScroll)
                lastIndex = currentIndex
                lastScroll = currentScroll
            }
        }
    }
}

fun NoteTag.toPublicName(): String {
    return when (this) {
        NoteTag.DAILY -> "Daily"
        NoteTag.BUSINESS -> "Business"
        NoteTag.EDUCATION -> "Education"
        NoteTag.NONE -> "No tag"
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

fun Uri.toFile(context: Context, suffix: String): File? {
    val inputStream = context.contentResolver.openInputStream(this)
    val tempFile = File.createTempFile("temp", suffix)
    return try {
        tempFile.outputStream().use { fileOut ->
            inputStream?.copyTo(fileOut)
        }
        tempFile.deleteOnExit()
        inputStream?.close()
        Log.d("MultiNote App: saved image file path -> ", tempFile.path)
        tempFile
    } catch (e: Exception) {
        null
    }
}

fun Int.getNoteType(): NoteType {
    return when (this) {
        NoteType.VIDEO.ordinal -> NoteType.VIDEO
        NoteType.AUDIO.ordinal -> NoteType.AUDIO
        NoteType.IMAGE.ordinal -> NoteType.IMAGE
        else -> NoteType.TEXT
    }
}