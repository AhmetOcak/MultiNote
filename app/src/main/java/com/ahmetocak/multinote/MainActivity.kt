package com.ahmetocak.multinote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.multinote.features.add_new_note.AddNewNoteScreen
import com.ahmetocak.multinote.core.ui.theme.MultiNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition(condition = {
            !viewModel.isPreferencesReady
        })
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (viewModel.isPreferencesReady) {
                MultiNoteTheme(
                    customColorScheme = uiState.colorScheme,
                    dynamicColor = uiState.isDynamicColor,
                    darkTheme = uiState.isDarkTheme
                ) {
                    AddNewNoteScreen()
                }
            }
        }
    }
}