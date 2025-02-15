package com.ahmetocak.multinote.data.repository.user_pref

import com.ahmetocak.multinote.model.ColorSchemeKeys
import com.ahmetocak.multinote.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateDarkTheme(darkTheme: Boolean)

    suspend fun updateColorScheme(colorScheme: ColorSchemeKeys)

    suspend fun updateDynamicColor(dynamicColor: Boolean)
}