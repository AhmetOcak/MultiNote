package com.ahmetocak.multinote.features.add_new_note

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.multinote.core.ui.components.MNTopBar
import com.ahmetocak.multinote.features.add_new_note.components.MediaBottomSheet
import com.ahmetocak.multinote.model.NoteTag
import com.ahmetocak.multinote.model.NoteType
import com.ahmetocak.multinote.utils.toStringResource

@Composable
fun AddNewNoteScreen(
    modifier: Modifier = Modifier,
    viewModel: AddNewNoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: AddNewNoteUiEvent -> viewModel.onEvent(event) }
    )

    AddNewNoteScreenContent(
        modifier = modifier,
        titleValue = uiState.titleValue,
        descriptionValue = uiState.descriptionValue,
        selectedNoteType = uiState.selectedNoteType,
        selectedNoteTag = uiState.selectedNoteTag,
        showMediaBottomSheet = uiState.showMediaBottomSheet,
        onEvent = onEvent,
        onAddMediaClick = viewModel::handleAddMedia
    )
}

@Composable
private fun AddNewNoteScreenContent(
    modifier: Modifier = Modifier,
    titleValue: String,
    descriptionValue: String,
    selectedNoteType: NoteType,
    selectedNoteTag: NoteTag,
    showMediaBottomSheet: Boolean,
    onEvent: (AddNewNoteUiEvent) -> Unit,
    onAddMediaClick: () -> Unit
) {
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
            SelectNoteType(
                selectedNoteType = selectedNoteType,
                onClick = {
                    onEvent(AddNewNoteUiEvent.OnTypeSelect(it))
                }
            )
            SelectNoteTag(
                selectedNoteTag = selectedNoteTag,
                onClick = {
                    onEvent(AddNewNoteUiEvent.OnTagSelect(it))
                }
            )

            NoteFoundations(
                value = titleValue,
                labelText = "Note Title",
                singleLine = true,
                onValueChange = {
                    onEvent(AddNewNoteUiEvent.OnTitleValueChange(it))
                }
            )
            NoteFoundations(
                value = descriptionValue,
                labelText = "Note Description",
                onValueChange = {
                    onEvent(AddNewNoteUiEvent.OnDescriptionValueChange(it))
                }
            )

            AnimatedVisibility(selectedNoteType == NoteType.IMAGE) {
                AddMediaContent(
                    text = "Click for the add Image",
                    icon = Icons.Default.Image,
                    onClick = onAddMediaClick
                )
            }
            AnimatedVisibility(selectedNoteType == NoteType.AUDIO) {
                AddMediaContent(
                    text = "Click for the add audio",
                    icon = Icons.Default.AudioFile,
                    onClick = onAddMediaClick
                )
            }
            AnimatedVisibility(selectedNoteType == NoteType.VIDEO) {
                AddMediaContent(
                    text = "Click for the add video",
                    icon = Icons.Default.VideoFile,
                    onClick = onAddMediaClick
                )
            }
            if (showMediaBottomSheet) {
                when (selectedNoteType) {
                    NoteType.IMAGE -> {
                        MediaBottomSheet(
                            action1Image = Icons.Default.Image,
                            action2Image = Icons.Default.CameraAlt,
                            action1OnClick = {},
                            action2OnClick = {}
                        )
                    }

                    NoteType.AUDIO -> {
                        MediaBottomSheet(
                            action1Image = Icons.Default.AudioFile,
                            action2Image = Icons.Default.Mic,
                            action1OnClick = {},
                            action2OnClick = {}
                        )
                    }

                    NoteType.VIDEO -> {
                        MediaBottomSheet(
                            action1Image = Icons.Default.VideoFile,
                            action2Image = Icons.Default.Videocam,
                            action1OnClick = {},
                            action2OnClick = {}
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun SelectNoteType(selectedNoteType: NoteType, onClick: (NoteType) -> Unit) {
    val context = LocalContext.current

    Column(modifier = Modifier.selectableGroup()) {
        Text(text = "Note Type", style = MaterialTheme.typography.labelLarge)
        NoteType.entries.forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .selectable(
                        selected = type == selectedNoteType,
                        onClick = {
                            onClick(type)
                        },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = type == selectedNoteType,
                    onClick = null
                )
                Text(
                    text = type.toStringResource(context),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            HorizontalDivider()
        }
    }
}

@Composable
private fun SelectNoteTag(selectedNoteTag: NoteTag, onClick: (NoteTag) -> Unit) {
    val context = LocalContext.current

    Column(modifier = Modifier.selectableGroup()) {
        Text(text = "Note Tag", style = MaterialTheme.typography.labelLarge)
        NoteTag.entries.forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .selectable(
                        selected = type == selectedNoteTag,
                        onClick = {
                            onClick(type)
                        },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = type == selectedNoteTag,
                    onClick = null
                )
                Text(
                    text = type.toStringResource(context),
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
private fun AddMediaContent(text: String, icon: ImageVector, onClick: () -> Unit) {
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
            },
        onClick = onClick
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
    AddNewNoteScreenContent(
        titleValue = "",
        descriptionValue = "",
        selectedNoteType = NoteType.TEXT,
        selectedNoteTag = NoteTag.NONE,
        onEvent = {},
        onAddMediaClick = {},
        showMediaBottomSheet = false
    )
}
