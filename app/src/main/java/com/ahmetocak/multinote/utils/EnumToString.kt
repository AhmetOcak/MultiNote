package com.ahmetocak.multinote.utils

import android.content.Context
import com.ahmetocak.multinote.model.NoteTag
import com.ahmetocak.multinote.model.NoteType

fun NoteTag.toStringResource(context: Context): String {
    return when (this) {
        NoteTag.DAILY -> "Daily"
        NoteTag.BUSINESS -> "Business"
        NoteTag.EDUCATION -> "Education"
    }
}

fun NoteType.toStringResource(context: Context): String {
    return when (this) {
        NoteType.TEXT -> "Text"
        NoteType.IMAGE -> "Image"
        NoteType.AUDIO -> "Audio"
        NoteType.VIDEO -> "Video"
    }
}