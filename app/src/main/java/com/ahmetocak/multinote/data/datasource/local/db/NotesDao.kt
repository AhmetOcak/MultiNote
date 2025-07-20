package com.ahmetocak.multinote.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ahmetocak.multinote.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert
    suspend fun addNote(note: Note): Long

    @Query("SELECT * FROM notes_table")
    fun observeNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes_table WHERE id = :id")
    fun getNoteById(id: Int): Flow<Note?>

    @Query("DELETE FROM notes_table WHERE id = :id")
    suspend fun deleteNote(id: Int)

    @Update
    suspend fun updateNote(note: Note)
}