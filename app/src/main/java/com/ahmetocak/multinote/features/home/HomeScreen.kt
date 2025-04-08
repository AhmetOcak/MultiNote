package com.ahmetocak.multinote.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentPasteSearch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.ahmetocak.multinote.utils.isScrollingUp
import com.ahmetocak.multinote.utils.toPublicName

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCreateNoteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onCardClick: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: HomeScreenUiEvent -> viewModel.onEvent(event) }
    )

    HomeScreenContent(
        modifier = modifier,
        searchQuery = viewModel.searchQuery,
        noteList = uiState.noteList,
        onCreateNoteClick = onCreateNoteClick,
        onSettingsClick = onSettingsClick,
        homeScreenState = uiState.screenState,
        onCardClick = onCardClick,
        onEvent = { onEvent(it) }
    )

    if (uiState.showFilterSheet) {
        FilterSheet(onEvent = onEvent, filterList = uiState.filterList)
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    searchQuery: String,
    noteList: List<Note>,
    onCreateNoteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    homeScreenState: HomeScreenState,
    onCardClick: (Int) -> Unit,
    onEvent: (HomeScreenUiEvent) -> Unit
) {
    val listState = rememberLazyStaggeredGridState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MNTopBar(
                title = "MultiNote",
                actions = {
                    if (homeScreenState is HomeScreenState.Idle) {
                        androidx.compose.animation.AnimatedVisibility(visible = true) {
                            IconButton(onClick = onSettingsClick) {
                                Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (homeScreenState is HomeScreenState.Idle) {
                AnimatedVisibility(
                    visible = listState.isScrollingUp().value,
                    enter = scaleIn(),
                    exit = scaleOut()

                ) {
                    FloatingActionButton(onClick = onCreateNoteClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        when (homeScreenState) {
            HomeScreenState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            HomeScreenState.Idle -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SearchField(
                        value = searchQuery,
                        onSearch = { onEvent(HomeScreenUiEvent.OnSearchClick) },
                        onFilterClick = { onEvent(HomeScreenUiEvent.OnShowFilterSheetClick) },
                        onValueChange = { onEvent(HomeScreenUiEvent.OnQueryEntering(it)) }
                    )
                    AnimatedVisibility(visible = noteList.isNotEmpty()) {
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            state = listState,
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
                                            description = it.description,
                                            onClick = { onCardClick(it.id) }
                                        )
                                    }

                                    NoteType.IMAGE.ordinal -> {
                                        ImageNoteCard(
                                            title = it.title,
                                            description = it.description,
                                            imagePath = it.imagePath,
                                            onClick = { onCardClick(it.id) }
                                        )
                                    }

                                    NoteType.AUDIO.ordinal -> {
                                        AudioNoteCard(
                                            title = it.title,
                                            description = it.description,
                                            onClick = { onCardClick(it.id) }
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSheet(filterList: List<Int>, onEvent: (HomeScreenUiEvent) -> Unit) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = { onEvent(HomeScreenUiEvent.OnCloseFilterSheetClick) },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Filter the note list",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            NoteTag.entries.toTypedArray().forEach { tag ->
                FilterItem(
                    filterName = tag.toPublicName(),
                    isChecked = filterList.contains(tag.ordinal),
                    onCheckedChange = { onEvent(HomeScreenUiEvent.OnFilterAction(tag.ordinal)) }
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                FilledTonalButton(onClick = { onEvent(HomeScreenUiEvent.OnCloseFilterSheetClick) }) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                FilledTonalButton(onClick = { onEvent(HomeScreenUiEvent.OnSubmitFilterClick) }) {
                    Text(text = "Filter")
                }
            }
        }
    }
}

@Composable
private fun FilterItem(
    isChecked: Boolean,
    filterName: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
        Text(text = filterName)
    }
}

@Composable
@Preview(showSystemUi = true, name = "EmptyList")
private fun PreviewHomeScreenWithEmpty() {
    HomeScreenContent(
        searchQuery = "",
        noteList = emptyList(),
        onSettingsClick = {},
        homeScreenState = HomeScreenState.Idle,
        onCreateNoteClick = {},
        onCardClick = {}
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
                audioPath = null,
                videoPath = null
            ),
            Note(
                id = 1,
                title = "Test Title",
                description = "AAewq ewqqwewq ewq weq wqe qw ewq eqwweq qwe",
                noteType = NoteType.AUDIO.ordinal,
                tag = NoteTag.DAILY.ordinal,
                imagePath = null,
                audioPath = null,
                videoPath = null
            ),
            Note(
                id = 2,
                title = "Test Title",
                description = "AAewq ewqqwewq ewq weq wqe qw ewq eqwweq qwe",
                noteType = NoteType.VIDEO.ordinal,
                tag = NoteTag.DAILY.ordinal,
                imagePath = null,
                audioPath = null,
                videoPath = null
            ),
            Note(
                id = 3,
                title = "Test Title",
                description = "AAewq ewqqwewq ewq weq wqe qw ewq eqwweq qwe",
                noteType = NoteType.IMAGE.ordinal,
                tag = NoteTag.DAILY.ordinal,
                imagePath = null,
                audioPath = null,
                videoPath = null
            )
        ),
        onSettingsClick = {},
        onCreateNoteClick = {},
        homeScreenState = HomeScreenState.Idle,
        onCardClick = {}
    ) { }
}