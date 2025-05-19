package com.ahmetocak.multinote.core.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.multinote.R
import com.ahmetocak.multinote.utils.getScreenHeight
import com.ahmetocak.multinote.utils.getVideoThumbnail

const val dummyDescription =
    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
            "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
val dummyImgId = R.drawable.test
private const val MAX_LINE = 12

@Composable
fun TextNoteCard(title: String, description: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
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
fun ImageNoteCard(title: String, description: String, imagePath: String?, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth().heightIn(max = getScreenHeight() / 3.5f),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imagePath)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit
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
fun AudioNoteCard(title: String, description: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
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
fun VideoNoteCard(title: String, description: String, videoPath: String?, onClick: () -> Unit) {
    val context = LocalContext.current
    val bitmap = if (videoPath != null) {
        remember(videoPath) {
            getVideoThumbnail(context, Uri.parse(videoPath))
        }
    } else null

    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            videoPath?.let {
                bitmap?.asImageBitmap()?.let { it1 ->
                    Box(modifier = Modifier.heightIn(max = getScreenHeight() / 3)) {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            bitmap = it1,
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }
                }
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