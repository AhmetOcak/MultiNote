package com.ahmetocak.multinote.features.note

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ahmetocak.multinote.core.navigation.Arguments
import com.ahmetocak.multinote.data.repository.note.NotesRepository
import com.ahmetocak.multinote.model.Note
import com.ahmetocak.multinote.utils.audio.player.AudioPlayer
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
    private val audioPlayer: AudioPlayer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    private val _playerState = MutableStateFlow<ExoPlayer?>(null)
    val playerState: StateFlow<ExoPlayer?> = _playerState

    private var currentPosition: Long = 0L

    init {
        val noteId = savedStateHandle.get<String>(Arguments.NOTE_ID)
        if (noteId != null) {
            getNoteWithId(noteId.toInt())
        } else {
            // TODO: Show error message
        }

        audioPlayer.initializeMediaPlayer(onCompletion = {
            _uiState.update {
                it.copy(isAudioPlaying = false)
            }
        })
    }

    private fun getNoteWithId(noteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.getNoteById(noteId).collect { note ->
                _uiState.update {
                    it.copy(
                        noteData = note,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onImageClick(imagePath: String?) {
        if (imagePath != null) {
            _uiState.update {
                it.copy(noteScreenState = NoteScreenState.FullScreenImage(imagePath))
            }
        }
    }

    fun resetScreenState() {
        _uiState.update {
            it.copy(noteScreenState = NoteScreenState.Default)
        }
    }

    fun onPlayAudioClick(audioPath: String) {
        viewModelScope.launch {
            if (audioPlayer.isPlaying()) {
                audioPlayer.pause()
                _uiState.update {
                    it.copy(isAudioPlaying = false)
                }
            } else {
                audioPlayer.play(audioPath)
                _uiState.update {
                    it.copy(isAudioPlaying = true)
                }
            }
        }
    }

    fun initializePlayer(context: Context, videoUrl: String) {
        if (_playerState.value == null) {
            viewModelScope.launch {
                val exoPlayer = ExoPlayer.Builder(context).build().also {
                    val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                    it.setMediaItem(mediaItem)
                    it.prepare()
                    it.playWhenReady = true
                    it.seekTo(currentPosition)
                    it.addListener(object : Player.Listener {
                        override fun onPlayerError(error: PlaybackException) {
                            handleError(error)
                        }
                    })
                }
                _playerState.value = exoPlayer
            }
        }
    }

    fun savePlayerState() {
        _playerState.value?.let {
            currentPosition = it.currentPosition
        }
    }

    fun releasePlayer() {
        _playerState.value?.release()
        _playerState.value = null
    }

    private fun handleError(error: PlaybackException) {
        when (error.errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
                // Handle network connection error
                println("Network connection error")
            }

            PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> {
                // Handle file not found error
                println("File not found")
            }

            PlaybackException.ERROR_CODE_DECODER_INIT_FAILED -> {
                // Handle decoder initialization error
                println("Decoder initialization error")
            }

            else -> {
                // Handle other types of errors
                println("Other error: ${error.message}")
            }
        }
    }

    fun onVideoItemClicked(videoPath: String) {
        _uiState.update {
            it.copy(noteScreenState = NoteScreenState.VideoState(videoPath))
        }
    }

    fun onBackClickedInVideoState() {
        _uiState.update {
            it.copy(noteScreenState = NoteScreenState.Default)
        }
    }

    override fun onCleared() {
        audioPlayer.releaseMediaPlayer()
        super.onCleared()
    }
}

data class NoteUiState(
    val isLoading: Boolean = true,
    val noteData: Note? = null,
    val isAudioPlaying: Boolean = false,
    val noteScreenState: NoteScreenState = NoteScreenState.Default,
)

sealed class NoteScreenState {
    data object Default : NoteScreenState()
    data class FullScreenImage(val imagePath: String) : NoteScreenState()
    data class VideoState(val videoPath: String) : NoteScreenState()
}