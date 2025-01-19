package com.ahmetocak.multinote.data.repository

import com.ahmetocak.multinote.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun addNote(note: Note)
    fun observeNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note
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