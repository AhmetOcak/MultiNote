package com.ahmetocak.multinote.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetocak.multinote.R

const val dummyDescription =
    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
            "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
val dummyImgId = R.drawable.test
private const val MAX_LINE = 12

@Composable
fun TextNoteCard(title: String, description: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = description,
                maxLines = MAX_LINE,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ImageNoteCard(title: String, description: String, imageId: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            NoteTitle(title)
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = description,
                maxLines = MAX_LINE,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AudioNoteCard(title: String, description: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier.size(96.dp),
                    imageVector = Icons.Default.AudioFile,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
            NoteTitle(title)
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = description,
                maxLines = MAX_LINE,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun NoteTitle(title: String) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = title,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
    )
}