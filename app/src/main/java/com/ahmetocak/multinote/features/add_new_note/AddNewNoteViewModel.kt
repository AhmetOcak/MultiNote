package com.ahmetocak.multinote.features.add_new_note

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ahmetocak.multinote.model.NoteTag
import com.ahmetocak.multinote.model.NoteType
import com.ahmetocak.multinote.utils.audio.recorder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddNewNoteViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder
) : ViewModel() {

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

            is AddNewNoteUiEvent.OnSaveNoteClick -> {}
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
}

data class AddNewNoteUiState(
    val selectedNoteType: NoteType = NoteType.TEXT,
    val selectedNoteTag: NoteTag = NoteTag.NONE,
    val titleValue: String = "",
    val descriptionValue: String = "",
    val isSaveReady: Boolean = false,
    val selectedImage: Uri? = null,
    val selectedVideo: Uri? = null,
    val selectedAudio: Uri? = null,
    val hideSheet: Boolean = false,
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