package com.ahmetocak.multinote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ahmetocak.multinote.features.add_new_note.AddNewNoteScreen
import com.ahmetocak.multinote.core.ui.theme.MultiNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiNoteTheme {
                AddNewNoteScreen()
            }
        }
    }
}