package com.ahmetocak.multinote.data.repository.note

import com.ahmetocak.multinote.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun addNote(note: Note): Long
    fun observeNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Flow<Note?>
    suspend fun deleteNote(id: Int)
    suspend fun updateNote(note: Note)
}