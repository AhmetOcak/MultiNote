package com.ahmetocak.multinote.features

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.multinote.core.ui.theme.color_schemes.CustomColorScheme
import com.ahmetocak.multinote.core.ui.theme.color_schemes.NeonColorScheme
import com.ahmetocak.multinote.core.ui.theme.color_schemes.OrangeColorScheme
import com.ahmetocak.multinote.core.ui.theme.color_schemes.PinkColorScheme
import com.ahmetocak.multinote.core.ui.theme.color_schemes.SeaColorScheme
import com.ahmetocak.multinote.core.ui.theme.color_schemes.YellowColorScheme
import com.ahmetocak.multinote.data.repository.user_pref.UserPreferencesRepository
import com.ahmetocak.multinote.model.ColorSchemeKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        observeUserPreferences()
    }

    var isPreferencesReady by mutableStateOf(false)
        private set

    private fun observeUserPreferences() {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.userPreferencesFlow.collect { preferences ->
                _uiState.update {
                    it.copy(
                        isDarkTheme = preferences.isDarkTheme,
                        isDynamicColor = preferences.isDynamicColor,
                        colorScheme = when (preferences.colorScheme) {
                            ColorSchemeKeys.YELLOW.name -> YellowColorScheme
                            ColorSchemeKeys.NEON.name -> NeonColorScheme
                            ColorSchemeKeys.ORANGE.name -> OrangeColorScheme
                            ColorSchemeKeys.SEA.name -> SeaColorScheme
                            else -> PinkColorScheme
                        }
                    )
                }
                isPreferencesReady = true
            }
        }
    }
}

data class MainUiState(
    val isDarkTheme: Boolean = false,
    val isDynamicColor: Boolean = false,
    val colorScheme: CustomColorScheme = PinkColorScheme
)