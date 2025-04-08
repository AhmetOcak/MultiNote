package com.ahmetocak.multinote.features.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.multinote.core.navigation.Arguments
import com.ahmetocak.multinote.data.repository.note.NotesRepository
import com.ahmetocak.multinote.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        val noteId = savedStateHandle.get<String>(Arguments.NOTE_ID)
        if (noteId != null) {
            getNoteWithId(noteId.toInt())
        } else {
            // TODO: Show error message
        }
    }

    private fun getNoteWithId(noteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.getNoteById(noteId).collect { note ->
                _uiState.update {
                    it.copy(
                        noteData = note,
                        isLoading = false,
                    )
                }
            }
        }
    }
}

data class NoteUiState(
    val isLoading: Boolean = true,
    val noteMediaPath: String? = null,
    val noteData: Note? = null
)