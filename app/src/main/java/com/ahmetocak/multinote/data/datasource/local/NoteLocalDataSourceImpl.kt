package com.ahmetocak.multinote.data.datasource.local

import com.ahmetocak.multinote.data.datasource.local.db.NotesDao
import com.ahmetocak.multinote.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteLocalDataSourceImpl @Inject constructor(
    private val notesDao: NotesDao
) : NoteLocalDataSource {
    override suspend fun addNote(note: Note) = notesDao.addNote(note)

    override fun observeNotes(): Flow<List<Note>> = notesDao.observeNotes()

    override suspend fun getNoteById(id: Int) = notesDao.getNoteById(id)

    override suspend fun deleteNote(id: Int) = notesDao.deleteNote(id)

    override suspend fun updateNote(note: Note) {
        return notesDao.updateNote(note)
    }
}