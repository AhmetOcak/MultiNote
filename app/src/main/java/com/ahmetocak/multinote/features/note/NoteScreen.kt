package com.ahmetocak.multinote.features.note

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.multinote.core.ui.components.AudioPlayer
import com.ahmetocak.multinote.core.ui.components.MNImage
import com.ahmetocak.multinote.core.ui.components.MNTopBar
import com.ahmetocak.multinote.core.ui.components.MNZoomableImage
import com.ahmetocak.multinote.core.ui.components.dummyDescription
import com.ahmetocak.multinote.model.Note
import com.ahmetocak.multinote.model.NoteTag
import com.ahmetocak.multinote.model.NoteType
import com.ahmetocak.multinote.utils.getAudioDuration
import com.ahmetocak.multinote.utils.getNoteType

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.noteScreenState) {
        is NoteScreenState.Default -> {
            uiState.noteData?.let { note ->
                NoteScreenContent(
                    modifier = modifier,
                    noteData = note,
                    onNavigateBack = onNavigateBack,
                    onImageClick = viewModel::onImageClick,
                    onPlayButtonClick = viewModel::onPlayAudioClick
                )
            } ?: {
                // TODO: SHOW ERROR SCREEN
            }
        }

        is NoteScreenState.FullScreenImage -> FullScreenImage(
            imagePath = state.imagePath,
            onBackClick = viewModel::resetScreenState
        )
    }
}

@Composable
private fun NoteScreenContent(
    modifier: Modifier = Modifier,
    noteData: Note,
    onNavigateBack: () -> Unit,
    onImageClick: (String?) -> Unit,
    onPlayButtonClick: (String) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MNTopBar(
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = {/*TODO: OPEN ACTIONS*/ }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = noteData.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(modifier = Modifier.padding(horizontal = 16.dp), text = noteData.description)
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Media",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                when (noteData.noteType.getNoteType()) {
                    NoteType.IMAGE -> {
                        items(noteData.imagePath ?: emptyList(), key = { it }) {
                            ImageItem(
                                imagePath = it,
                                onClick = { onImageClick(it) }
                            )
                        }
                    }

                    NoteType.VIDEO -> {}
                    NoteType.AUDIO -> {
                        itemsIndexed(noteData.audioPath ?: emptyList()) { index, content ->
                            AudioPlayer(
                                isAudioPlaying = selectedIndex == index,
                                duration = getAudioDuration(context, Uri.parse(content)),
                                onPlayButtonClicked = {
                                    selectedIndex = if (selectedIndex == index)
                                        -1
                                    else
                                        index
                                    onPlayButtonClick(content)
                                }
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun ImageItem(imagePath: String?, onClick: () -> Unit) {
    Card(onClick = onClick) {
        MNImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f),
            imagePath = imagePath,
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
private fun FullScreenImage(imagePath: String, onBackClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .zIndex(1f),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null
                )
            }
        }
        MNZoomableImage(
            modifier = Modifier.fillMaxSize(),
            imagePath = imagePath,
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
@Preview
private fun PreviewNoteScreenContent() {
    NoteScreenContent(
        onNavigateBack = {},
        noteData = Note(
            noteType = NoteType.TEXT.ordinal,
            title = "note title",
            description = dummyDescription,
            tag = NoteTag.DAILY.ordinal,
            audioPath = emptyList(),
            imagePath = emptyList(),
            videoPath = emptyList()
        ),
        onImageClick = {},
        onPlayButtonClick = {_: String ->  }
    )
}