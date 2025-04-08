package com.ahmetocak.multinote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "note_type")
    val noteType: Int,

    @ColumnInfo(name = "tag")
    val tag: Int,

    @ColumnInfo(name = "image_path")
    val imagePath: List<String>?,

    @ColumnInfo(name = "audio_path")
    val audioPath: List<String>?,

    @ColumnInfo(name = "video_path")
    val videoPath: List<String>?
)
