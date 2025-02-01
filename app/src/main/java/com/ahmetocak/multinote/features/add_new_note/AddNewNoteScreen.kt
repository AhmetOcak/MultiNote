package com.ahmetocak.multinote.features.add_new_note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahmetocak.multinote.core.ui.components.MNTopBar
import com.ahmetocak.multinote.model.NoteTag
import com.ahmetocak.multinote.model.NoteType
import com.ahmetocak.multinote.utils.toStringResource
import kotlin.enums.EnumEntries

@Composable
fun AddNewNoteScreen() {

}

@Composable
private fun AddNewNoteScreenContent(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MNTopBar(title = "Create Note")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NoteSpecialSettings(
                title = "Note Type",
                entries = NoteType.entries
            )
            NoteSpecialSettings(
                title = "Note Tag",
                entries = NoteTag.entries
            )

            NoteFoundations(
                value = "",
                labelText = "Note Title",
                singleLine = true,
                onValueChange = {}
            )
            NoteFoundations(
                value = "",
                labelText = "Note Description",
                onValueChange = {}
            )

            AddMediaContent(
                text = "Click for the add Image",
                icon = Icons.Default.Image
            )
            AddMediaContent(
                text = "Click for the add audio",
                icon = Icons.Default.AudioFile
            )
            AddMediaContent(
                text = "Click for the add video",
                icon = Icons.Default.VideoFile
            )
        }
    }
}

@Composable
private fun NoteSpecialSettings(title: String, entries: EnumEntries<*>) {
    val context = LocalContext.current

    Column(modifier = Modifier.selectableGroup()) {
        Text(text = title, style = MaterialTheme.typography.labelLarge)
        entries.forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .selectable(
                        selected = false,
                        onClick = {},
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = false,
                    onClick = null
                )
                Text(
                    text = when (type) {
                        is NoteTag -> type.toStringResource(context)
                        is NoteType -> type.toStringResource(context)
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            HorizontalDivider()
        }
    }
}

@Composable
private fun NoteFoundations(
    value: String,
    labelText: String,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (!singleLine)
                    Modifier.height(168.dp)
                else Modifier
            ),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = singleLine,
        label = {
            Text(text = labelText)
        },
        placeholder = {
            Text(text = labelText)
        }
    )
}

@Composable
private fun AddMediaContent(text: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color.Black,
                    style = Stroke(
                        width = 2f, pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(10f, 10f), 0f
                        )
                    ),
                    cornerRadius = CornerRadius(12.dp.toPx())
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(56.dp),
                imageVector = icon,
                contentDescription = null
            )
            Spacer(Modifier.height(8.dp))
            Text(text = text)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewAddNewNoteScreen() {
    AddNewNoteScreenContent()
}