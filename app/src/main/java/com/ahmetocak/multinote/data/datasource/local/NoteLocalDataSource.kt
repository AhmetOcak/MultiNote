package com.ahmetocak.multinote.data.datasource.local

import com.ahmetocak.multinote.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteLocalDataSource {
    suspend fun addNote(note: Note): Long
    fun observeNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Flow<Note?>
    suspend fun deleteNote(id: Int)
    suspend fun updateNote(
        id: Int,
        title: String,
        description: String,
        imagePath: String?,
        audioPath: String?,
        tag: Int
    )
}