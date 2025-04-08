package com.ahmetocak.multinote.features.add_new_note

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.multinote.data.repository.note.NotesRepository
import com.ahmetocak.multinote.model.Note
import com.ahmetocak.multinote.model.NoteTag
import com.ahmetocak.multinote.model.NoteType
import com.ahmetocak.multinote.utils.audio.recorder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewNoteViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddNewNoteUiState())
    val uiState: StateFlow<AddNewNoteUiState> = _uiState.asStateFlow()

    fun onEvent(event: AddNewNoteUiEvent) {
        when (event) {
            is AddNewNoteUiEvent.OnTypeSelect -> _uiState.update {
                it.copy(
                    selectedNoteType = event.type,
                    selectedVideo = null,
                    selectedImage = null,
                    selectedAudio = null
                )
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

            is AddNewNoteUiEvent.OnRecordAudioClick -> {
                when (_uiState.value.audioRecordStatus) {
                    is AudioRecordStatus.IDLE -> {
                        _uiState.update {
                            it.copy(audioRecordStatus = AudioRecordStatus.RECORDING)
                        }
                        audioRecorder.startRecording(1)
                    }

                    is AudioRecordStatus.RECORDING -> {
                        _uiState.update {
                            it.copy(audioRecordStatus = AudioRecordStatus.IDLE)
                        }
                        val recordedAudioFile = audioRecorder.stopRecording()
                        Log.i("MN App: Recorded file path ", recordedAudioFile.path)
                    }
                }
            }

            is AddNewNoteUiEvent.OnSaveNoteClick -> addNote()
        }
    }

    fun handleAction1Click(uri: Uri) {
        when (_uiState.value.selectedNoteType) {
            NoteType.IMAGE -> _uiState.update {
                it.copy(selectedImage = uri)
            }

            NoteType.AUDIO -> _uiState.update {
                it.copy(selectedAudio = uri)
            }

            NoteType.VIDEO -> _uiState.update {
                it.copy(selectedVideo = uri)
            }

            else -> {}
        }
    }

    fun isSaveReady(): Boolean {
        val state = _uiState.value
        val isMandatoryFieldsFill =
            state.titleValue.isNotEmpty() && state.descriptionValue.isNotEmpty()
        return when (state.selectedNoteType) {
            NoteType.TEXT -> isMandatoryFieldsFill
            NoteType.IMAGE -> isMandatoryFieldsFill && state.selectedImage != null
            NoteType.AUDIO -> isMandatoryFieldsFill && state.selectedAudio != null
            NoteType.VIDEO -> isMandatoryFieldsFill && state.selectedVideo != null
        }
    }

    private fun addNote() {
        viewModelScope.launch(Dispatchers.IO) {
            val state = _uiState.value
            try {
                val result = notesRepository.addNote(
                    note = Note(
                        title = state.titleValue,
                        description = state.titleValue,
                        tag = state.selectedNoteTag.ordinal,
                        noteType = state.selectedNoteType.ordinal,
                        audioPath = state.selectedAudio?.path,
                        imagePath = state.selectedImage?.path,
                        videoPath = state.selectedVideo?.path
                    )
                )

                if (result != -1L && result >= 0L) {
                    _uiState.update {
                        it.copy(showSaveNoteSuccessMessage = true)
                    }
                }

                viewModelScope.launch {
                    delay(3000)
                    _uiState.update {
                        it.copy(showSaveNoteSuccessMessage = false)
                    }
                }
                resetAllData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun resetAllData() {
        _uiState.update {
            it.copy(
                selectedNoteType = NoteType.TEXT,
                selectedNoteTag = NoteTag.NONE,
                titleValue = "",
                descriptionValue = "",
                selectedImage = null,
                selectedVideo = null,
                selectedAudio = null
            )
        }
    }
}

data class AddNewNoteUiState(
    val selectedNoteType: NoteType = NoteType.TEXT,
    val selectedNoteTag: NoteTag = NoteTag.NONE,
    val titleValue: String = "",
    val descriptionValue: String = "",
    val selectedImage: Uri? = null,
    val selectedVideo: Uri? = null,
    val selectedAudio: Uri? = null,
    val hideSheet: Boolean = false,
    val showSaveNoteSuccessMessage: Boolean = false,
    val audioRecordStatus: AudioRecordStatus = AudioRecordStatus.IDLE
)

sealed class AddNewNoteUiEvent {
    data class OnTypeSelect(val type: NoteType) : AddNewNoteUiEvent()
    data class OnTagSelect(val tag: NoteTag) : AddNewNoteUiEvent()
    data class OnTitleValueChange(val value: String) : AddNewNoteUiEvent()
    data class OnDescriptionValueChange(val value: String) : AddNewNoteUiEvent()
    data object OnRecordAudioClick : AddNewNoteUiEvent()
    data object OnSaveNoteClick : AddNewNoteUiEvent()
}

sealed interface AudioRecordStatus {
    data object RECORDING : AudioRecordStatus
    data object IDLE : AudioRecordStatus
}

sealed interface AudioPlayStatus {
    data object PLAYING : AudioPlayStatus
    data object IDLE : AudioPlayStatus
}