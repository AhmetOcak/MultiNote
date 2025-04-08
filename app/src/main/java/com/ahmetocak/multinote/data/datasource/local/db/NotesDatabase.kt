package com.ahmetocak.multinote.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ahmetocak.multinote.model.Note
import com.ahmetocak.multinote.utils.Converters

@Database(
    entities = [Note::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao() : NotesDao
}