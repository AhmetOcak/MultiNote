package com.ahmetocak.multinote.features.note

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ahmetocak.multinote.core.ui.components.MNTopBar
import com.ahmetocak.multinote.core.ui.components.dummyDescription

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: NoteViewModel = hiltViewModel()
) {

    NoteScreenContent(modifier = modifier, onNavigateBack = onNavigateBack)
}

@Composable
private fun NoteScreenContent(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {
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
                .padding(16.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "NOTE TITLE",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = dummyDescription)

            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = Uri.parse(""),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
@Preview
private fun PreviewNoteScreenContent() {
    NoteScreenContent(onNavigateBack = {})
}