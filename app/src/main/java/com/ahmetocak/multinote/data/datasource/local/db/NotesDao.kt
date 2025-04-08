package com.ahmetocak.multinote.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ahmetocak.multinote.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert
    suspend fun addNote(note: Note): Long

    @Query("SELECT * FROM notes_table")
    fun observeNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes_table WHERE id = :id")
    suspend fun getNoteById(id: Int): Note

    @Query("DELETE FROM notes_table WHERE id = :id")
    suspend fun deleteNote(id: Int)

    @Query("UPDATE notes_table SET title = :title AND description = :description AND image_path = :imagePath AND audio_path = :audioPath AND tag = :tag WHERE id = :id")
    suspend fun updateNote(
        id: Int,
        title: String,
        description: String,
        imagePath: String?,
        audioPath: String?,
        tag: Int
    )
}