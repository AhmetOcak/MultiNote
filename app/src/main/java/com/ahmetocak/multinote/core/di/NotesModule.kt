package com.ahmetocak.multinote.core.di

import android.content.Context
import androidx.room.Room
import com.ahmetocak.multinote.data.datasource.local.NoteLocalDataSource
import com.ahmetocak.multinote.data.datasource.local.NoteLocalDataSourceImpl
import com.ahmetocak.multinote.data.datasource.local.db.NotesDao
import com.ahmetocak.multinote.data.datasource.local.db.NotesDatabase
import com.ahmetocak.multinote.data.repository.NotesRepository
import com.ahmetocak.multinote.data.repository.NotesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesModule {

    @Provides
    @Singleton
    fun provideNotesDb(@ApplicationContext context: Context): NotesDatabase {
        return Room.databaseBuilder(
            context = context,
            name = "notes_db",
            klass = NotesDatabase::class.java
        ).build()
    }

    @Provides
    @Singleton
    fun provideNotesDao(notesDb: NotesDatabase): NotesDao {
        return notesDb.notesDao()
    }

    @Provides
    @Singleton
    fun provideNotesLocalDataSource(notesDao: NotesDao): NoteLocalDataSource {
        return NoteLocalDataSourceImpl(notesDao)
    }

    @Provides
    @Singleton
    fun provideNotesRepository(notesLocalDataSource: NoteLocalDataSource): NotesRepository {
        return NotesRepositoryImpl(notesLocalDataSource)
    }
}