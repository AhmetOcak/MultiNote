package com.ahmetocak.multinote.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.ahmetocak.multinote.data.repository.user_pref.UserPreferencesRepository
import com.ahmetocak.multinote.data.repository.user_pref.UserPreferencesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DatastorePreferences(val appPreferences: AppPreferences)

enum class AppPreferences { USER_PREF }

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @DatastorePreferences(AppPreferences.USER_PREF)
    fun provideUserPreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("user_preferences_datastore")
            }
        )
    }

    @Provides
    @Singleton
    fun provideUserPrefRepository(
        @DatastorePreferences(AppPreferences.USER_PREF) dataStore: DataStore<Preferences>
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(dataStore)
    }
}