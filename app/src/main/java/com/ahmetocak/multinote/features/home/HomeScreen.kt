package com.ahmetocak.multinote.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPasteSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.multinote.core.ui.components.AudioNoteCard
import com.ahmetocak.multinote.core.ui.components.ImageNoteCard
import com.ahmetocak.multinote.core.ui.components.MNTopBar
import com.ahmetocak.multinote.core.ui.components.SearchField
import com.ahmetocak.multinote.core.ui.components.TextNoteCard
import com.ahmetocak.multinote.model.Note
import com.ahmetocak.multinote.model.NoteTag
import com.ahmetocak.multinote.model.NoteType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { _: HomeScreenUiEvent -> viewModel::onEvent }
    )
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    HomeScreenContent(
        searchQuery = viewModel.searchQuery,
        noteList = uiState.noteList,
        onOpenFiltersClick = {
            coroutineScope.launch {
                sheetState.show()
            }
        },
        onEvent = { onEvent(it) }
    )
}

@Composable
fun HomeScreenContent(
    searchQuery: String,
    noteList: List<Note>,
    onOpenFiltersClick: () -> Unit,
    onEvent: (HomeScreenUiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MNTopBar(title = "MultiNote")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchField(
                value = searchQuery,
                onSearch = { onEvent(HomeScreenUiEvent.OnSearchClick) },
                onFilterClick = onOpenFiltersClick,
                onValueChange = { onEvent(HomeScreenUiEvent.OnQueryEntering(it)) }
            )
            AnimatedVisibility(visible = noteList.isNotEmpty()) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp,
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(noteList, key = { it.id }) {
                        when (it.noteType) {
                            NoteType.TEXT.ordinal -> {
                                TextNoteCard(
                                    title = it.title,
                                    description = it.description
                                )
                            }

                            NoteType.IMAGE.ordinal -> {
                                ImageNoteCard(
                                    title = it.title,
                                    description = it.description,
                                    imagePath = it.imagePath
                                )
                            }

                            NoteType.AUDIO.ordinal -> {
                                AudioNoteCard(
                                    title = it.title,
                                    description = it.description
                                )
                            }

                            NoteType.VIDEO.ordinal -> {

                            }
                        }
                    }
                }
            }
            if (noteList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(128.dp),
                        imageVector = Icons.Default.ContentPasteSearch,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "There is no notes...")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSheet(sheetState: SheetState) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        onDismissRequest = {},
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Filter the note list",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            /*
            TODO: Filter list
             */
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                FilledTonalButton(onClick = {}) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                FilledTonalButton(onClick = {}) {
                    Text(text = "Filter")
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, name = "EmptyList")
private fun PreviewHomeScreenWithEmpty() {
    HomeScreenContent(
        searchQuery = "",
        noteList = emptyList(),
        onOpenFiltersClick = {}
    ) { }
}

@Composable
@Preview(showSystemUi = true)
private fun PreviewHomeScreen() {
    HomeScreenContent(
        searchQuery = "",
        noteList = listOf(
            Note(
                id = 0,
                title = "Test Title",
                description = "AAewq ewqqwewq ewq weq wqe qw ewq eqwweq qwe",
                noteType = NoteType.TEXT.ordinal,
                tag = NoteTag.DAILY.ordinal,
                imagePath = null,
                audioPath = null
            ),
            Note(
                id = 1,
                title = "Test Title",
                description = "AAewq ewqqwewq ewq weq wqe qw ewq eqwweq qwe",
                noteType = NoteType.AUDIO.ordinal,
                tag = NoteTag.DAILY.ordinal,
                imagePath = null,
                audioPath = null
            ),
            Note(
                id = 2,
                title = "Test Title",
                description = "AAewq ewqqwewq ewq weq wqe qw ewq eqwweq qwe",
                noteType = NoteType.VIDEO.ordinal,
                tag = NoteTag.DAILY.ordinal,
                imagePath = null,
                audioPath = null
            ),
            Note(
                id = 3,
                title = "Test Title",
                description = "AAewq ewqqwewq ewq weq wqe qw ewq eqwweq qwe",
                noteType = NoteType.IMAGE.ordinal,
                tag = NoteTag.DAILY.ordinal,
                imagePath = null,
                audioPath = null
            )
        ),
        onOpenFiltersClick = {}
    ) { }
}