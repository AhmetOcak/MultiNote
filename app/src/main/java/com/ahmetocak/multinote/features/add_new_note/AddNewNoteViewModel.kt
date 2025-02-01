package com.ahmetocak.multinote.features.add_new_note

import androidx.lifecycle.ViewModel
import com.ahmetocak.multinote.model.NoteTag
import com.ahmetocak.multinote.model.NoteType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddNewNoteViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AddNewNoteUiState())
    val uiState: StateFlow<AddNewNoteUiState> = _uiState.asStateFlow()

    fun onEvent(event: AddNewNoteUiEvent) {
        when (event) {
            is AddNewNoteUiEvent.OnTypeSelect -> _uiState.update {
                it.copy(selectedNoteType = event.type)
            }

            is AddNewNoteUiEvent.OnTagSelect -> _uiState.update {
                it.copy(selectedNoteTag = event.tag)
            }

            is AddNewNoteUiEvent.OnTitleValueChange -> _uiState.update {
                it.copy(titleValue = event.value)
            }

            is AddNewNoteUiEvent.OnDescriptionValueChange -> _uiState.update {
                it.copy(descriptionValue = event.value)
            }
        }
    }

    fun handleAddMedia() {

    }
}

data class AddNewNoteUiState(
    val selectedNoteType: NoteType = NoteType.TEXT,
    val selectedNoteTag: NoteTag = NoteTag.NONE,
    val titleValue: String = "",
    val descriptionValue: String = "",
    val showMediaBottomSheet: Boolean = false
)

sealed class AddNewNoteUiEvent {
    data class OnTypeSelect(val type: NoteType) : AddNewNoteUiEvent()
    data class OnTagSelect(val tag: NoteTag) : AddNewNoteUiEvent()
    data class OnTitleValueChange(val value: String) : AddNewNoteUiEvent()
    data class OnDescriptionValueChange(val value: String) : AddNewNoteUiEvent()
}