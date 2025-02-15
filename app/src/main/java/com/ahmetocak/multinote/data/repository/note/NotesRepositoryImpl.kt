package com.ahmetocak.multinote.data.repository.note

import com.ahmetocak.multinote.data.datasource.local.NoteLocalDataSource
import com.ahmetocak.multinote.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesLocalDataSource: NoteLocalDataSource
): NotesRepository {
    override suspend fun addNote(note: Note) = notesLocalDataSource.addNote(note)

    override fun observeNotes(): Flow<List<Note>> = notesLocalDataSource.observeNotes()

    override suspend fun getNoteById(id: Int): Note = notesLocalDataSource.getNoteById(id)

    override suspend fun deleteNote(id: Int) = notesLocalDataSource.deleteNote(id)

    override suspend fun updateNote(
        id: Int,
        title: String,
        description: String,
        imagePath: String?,
        audioPath: String?,
        tag: Int
    ) {
        return notesLocalDataSource.updateNote(id, title, description, imagePath, audioPath, tag)
    }
}