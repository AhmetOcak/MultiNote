package com.ahmetocak.multinote.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmetocak.multinote.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao() : NotesDao
}