package com.ahmetocak.multinote.features.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.multinote.data.repository.note.NotesRepository
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

    private var originalNoteList = emptyList<Note>()

    var searchQuery by mutableStateOf("")
        private set

    init {
        observeNotes()
    }

    fun onEvent(event: HomeScreenUiEvent) {
        when (event) {
            is HomeScreenUiEvent.OnNoteClick -> {

            }

            is HomeScreenUiEvent.OnQueryEntering -> {
                searchQuery = event.query
                filterListByQuery()
            }

            is HomeScreenUiEvent.OnSearchClick -> filterListByQuery()

            is HomeScreenUiEvent.OnShowFilterSheetClick -> _uiState.update {
                it.copy(showFilterSheet = true)
            }

            is HomeScreenUiEvent.OnCloseFilterSheetClick -> _uiState.update {
                it.copy(showFilterSheet = false, filterList = emptyList())
            }

            is HomeScreenUiEvent.OnSubmitFilterClick -> {
                if (_uiState.value.filterList.isEmpty()) {
                    _uiState.update {
                        it.copy(showFilterSheet = false, noteList = originalNoteList)
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            showFilterSheet = false,
                            noteList = originalNoteList.filter { note ->
                                _uiState.value.filterList.any { filter -> filter == note.tag }
                            }
                        )
                    }
                }
            }

            is HomeScreenUiEvent.OnFilterAction -> {
                val currentList = _uiState.value.filterList.toMutableList()
                if (currentList.contains(event.filterOrd)) {
                    currentList.remove(event.filterOrd)
                } else {
                    currentList.add(event.filterOrd)
                }
                _uiState.update {
                    it.copy(filterList = currentList)
                }
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
                    originalNoteList = noteList
                }
        }
    }

    private fun filterListByQuery() {
        val filteredList = originalNoteList.filter { it.title.contains(searchQuery) }
        _uiState.update {
            it.copy(noteList = if (searchQuery.isEmpty()) originalNoteList else filteredList)
        }
    }
}

data class HomeScreenUiState(
    val showFilterSheet: Boolean = false,
    val filterList: List<Int> = emptyList(),
    val noteList: List<Note> = emptyList(),
    val screenState: HomeScreenState = HomeScreenState.Loading
)

sealed interface HomeScreenState {
    data object Loading : HomeScreenState
    data object Idle : HomeScreenState
}

sealed class HomeScreenUiEvent {
    data class OnNoteClick(val id: Inject) : HomeScreenUiEvent()
    data class OnQueryEntering(val query: String) : HomeScreenUiEvent()
    data object OnSearchClick : HomeScreenUiEvent()
    data object OnSubmitFilterClick : HomeScreenUiEvent()
    data object OnShowFilterSheetClick : HomeScreenUiEvent()
    data object OnCloseFilterSheetClick : HomeScreenUiEvent()
    data class OnFilterAction(val filterOrd: Int) : HomeScreenUiEvent()
}