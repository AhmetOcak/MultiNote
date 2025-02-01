package com.ahmetocak.multinote.features.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.multinote.data.repository.NotesRepository
import com.ahmetocak.multinote.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    init {
        observeNotes()
    }

    fun onEvent(event: HomeScreenUiEvent) {
        when (event) {
            is HomeScreenUiEvent.OnNoteClick -> {

            }

            is HomeScreenUiEvent.OnQueryEntering -> searchQuery = event.query

            is HomeScreenUiEvent.OnSearchClick -> {
                val filteredList = _uiState.value.noteList.filter { it.title.contains(searchQuery) }
                _uiState.update {
                    it.copy(noteList = filteredList)
                }
            }

            is HomeScreenUiEvent.OnFilterListClick -> {

            }
        }
    }

    private fun observeNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.observeNotes()
                .catch { throwable ->
                    Log.e("observeNotes", throwable.message ?: "Something went wrong")
                    _uiState.update { it.copy(screenState = HomeScreenState.Idle) }
                }
                .collect { noteList ->
                    _uiState.update {
                        it.copy(screenState = HomeScreenState.Idle, noteList = noteList)
                    }
                }
        }
    }
}

data class HomeScreenUiState(
    val noteList: List<Note> = emptyList(),
    val screenState: HomeScreenState = HomeScreenState.Loading
)

sealed interface HomeScreenState {
    data object Loading : HomeScreenState
    data object Idle : HomeScreenState
    data object Filter : HomeScreenState
}

sealed class HomeScreenUiEvent {
    data class OnNoteClick(val id: Inject) : HomeScreenUiEvent()
    data class OnQueryEntering(val query: String) : HomeScreenUiEvent()
    data object OnSearchClick : HomeScreenUiEvent()
    data object OnFilterListClick : HomeScreenUiEvent()
}