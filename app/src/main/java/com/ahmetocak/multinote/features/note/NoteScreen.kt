package com.ahmetocak.multinote.features.note

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.multinote.R
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
import com.ahmetocak.multinote.utils.getVideoThumbnail

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onEditClick: (Int) -> Unit,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MNTopBar(
                onNavigateBack = {
                    if (uiState.noteScreenState is NoteScreenState.Default) {
                        onNavigateBack.invoke()
                    } else {
                        viewModel.resetScreenState()
                    }
                },
                actions = {
                    if (uiState.noteScreenState is NoteScreenState.Default) {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = uiState.noteScreenState) {
            is NoteScreenState.Default -> {
                uiState.noteData?.let { note ->
                    MenuSection(
                        padding = innerPadding,
                        expanded = expanded,
                        onDismiss = { expanded = false },
                        onEdit = { onEditClick.invoke(note.id) },
                        onArchive = {},
                        onDelete = {
                            viewModel.deleteNote {
                                onNavigateBack.invoke()
                            }
                        }
                    )
                    NoteScreenContent(
                        modifier = modifier.padding(innerPadding),
                        noteData = note,
                        isAudioPlaying = uiState.isAudioPlaying,
                        currentAudioPos = uiState.currentAudioPos,
                        increaseCurrAudioPos = viewModel::increaseCurrentAudioPos,
                        resetCurrAudioPos = viewModel::resetCurrentAudioPos,
                        onImageClick = viewModel::onImageClick,
                        onPlayButtonClick = viewModel::onPlayAudioClick,
                        onVideoClick = viewModel::onVideoItemClicked
                    )
                } ?: {

                }
            }

            is NoteScreenState.FullScreenImage -> FullScreenImage(
                modifier = modifier.padding(innerPadding),
                imagePath = state.imagePath
            )

            is NoteScreenState.VideoState -> VideoPlayer(
                modifier = modifier.padding(innerPadding),
                videoPath = state.videoPath,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun NoteScreenContent(
    modifier: Modifier = Modifier,
    noteData: Note,
    isAudioPlaying: Boolean,
    currentAudioPos: Int,
    increaseCurrAudioPos: () -> Unit,
    resetCurrAudioPos: () -> Unit,
    onImageClick: (String?) -> Unit,
    onPlayButtonClick: (String) -> Unit,
    onVideoClick: (String) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = noteData.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(modifier = Modifier.padding(horizontal = 16.dp), text = noteData.description)
        if (!noteData.imagePath.isNullOrEmpty() || !noteData.audioPath.isNullOrEmpty() || !noteData.videoPath.isNullOrEmpty()) {
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

                    NoteType.VIDEO -> {
                        itemsIndexed(noteData.videoPath ?: emptyList()) { _, item ->
                            VideoItem(videoPath = item, onClick = { onVideoClick(item) })
                        }
                    }

                    NoteType.AUDIO -> {
                        itemsIndexed(noteData.audioPath ?: emptyList()) { index, content ->
                            AudioPlayer(
                                isAudioPlaying = isAudioPlaying && selectedIndex == index,
                                duration = getAudioDuration(context, Uri.parse(content)),
                                currentAudioPosition = if (selectedIndex == index)
                                    currentAudioPos
                                else 0,
                                increaseCurrentAudioPosition = increaseCurrAudioPos,
                                resetCurrentPosition = resetCurrAudioPos,
                                onPlayButtonClicked = {
                                    onPlayButtonClick(content)
                                    selectedIndex = index
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
private fun FullScreenImage(modifier: Modifier = Modifier, imagePath: String) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MNZoomableImage(
            modifier = Modifier.fillMaxSize(),
            imagePath = imagePath,
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun VideoItem(videoPath: String?, onClick: () -> Unit) {
    Card(onClick = onClick) {
        val context = LocalContext.current
        var bitmap: Bitmap? by remember { mutableStateOf(null) }

        LaunchedEffect(true) {
            bitmap = if (videoPath != null) {
                getVideoThumbnail(context, Uri.parse(videoPath))
            } else null
        }
        videoPath?.let {
            bitmap?.asImageBitmap()?.let { it1 ->
                Image(
                    modifier = Modifier.aspectRatio(4f / 3f),
                    bitmap = it1,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Composable
private fun MenuSection(
    padding: PaddingValues,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onArchive: () -> Unit,
    onEdit: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(padding)
            .padding(end = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
            .zIndex(1f),
        contentAlignment = Alignment.CenterEnd
    ) {
        AnimatedVisibility(visible = expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismiss
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.edit)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    },
                    onClick = onEdit
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.archive)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Archive, contentDescription = null)
                    },
                    onClick = onArchive
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.delete),
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    onClick = onDelete
                )
            }
        }
    }
}

@Composable
@Preview
private fun PreviewNoteScreenContent() {
    NoteScreenContent(
        noteData = Note(
            noteType = NoteType.TEXT.ordinal,
            title = "note title",
            description = dummyDescription,
            tag = NoteTag.DAILY.ordinal,
            audioPath = emptyList(),
            imagePath = emptyList(),
            videoPath = emptyList()
        ),
        isAudioPlaying = false,
        currentAudioPos = 0,
        increaseCurrAudioPos = {},
        resetCurrAudioPos = {},
        onImageClick = {},
        onPlayButtonClick = { _: String -> },
        onVideoClick = {}
    )
}