package com.ahmetocak.multinote.features.note

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.multinote.R
import com.ahmetocak.multinote.core.ui.components.MNTopBar
import com.ahmetocak.multinote.core.ui.components.dummyDescription

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.noteData?.let { note ->
        NoteScreenContent(
            modifier = modifier,
            noteTitle = note.title,
            noteDescription = note.description,
            onNavigateBack = onNavigateBack
        )
    } ?: {
        // TODO: SHOW ERROR SCREEN
    }
}

@Composable
private fun NoteScreenContent(
    modifier: Modifier = Modifier,
    noteTitle: String,
    noteDescription: String,
    onNavigateBack: () -> Unit
) {
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
                text = noteTitle,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(modifier = Modifier.padding(horizontal = 16.dp), text = noteDescription)
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Media",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(6) {
                    MediaItem { }
                }
            }
        }
    }
}

@Composable
private fun MediaItem(onClick: () -> Unit) {
    val width = LocalConfiguration.current.screenWidthDp.dp * 0.75f
    Card(onClick = onClick) {
        Image(
            modifier = Modifier
                .width(width)
                .aspectRatio(4f / 3f),
            painter = painterResource(R.drawable.test),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
@Preview
private fun PreviewNoteScreenContent() {
    NoteScreenContent(onNavigateBack = {}, noteTitle = "note title", noteDescription = dummyDescription)
}