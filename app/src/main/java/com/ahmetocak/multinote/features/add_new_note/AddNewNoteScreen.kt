package com.ahmetocak.multinote.features.add_new_note

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ahmetocak.multinote.core.ui.components.MNTopBar
import com.ahmetocak.multinote.core.ui.components.ProcessResultWarning
import com.ahmetocak.multinote.features.add_new_note.components.MediaBottomSheet
import com.ahmetocak.multinote.model.NoteTag
import com.ahmetocak.multinote.model.NoteType
import com.ahmetocak.multinote.utils.getAudioDuration
import com.ahmetocak.multinote.utils.getVideoThumbnail
import com.ahmetocak.multinote.utils.toStringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddNewNoteScreen(
    modifier: Modifier = Modifier,
    onNavigateUpClick: () -> Unit,
    viewModel: AddNewNoteViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            listOf(Manifest.permission.CAMERA)
        } else listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: AddNewNoteUiEvent -> viewModel.onEvent(event) }
    )

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var createdMediaFile by rememberSaveable { mutableStateOf<Uri?>(null) }

    val createMediaFile: (String) -> Uri = {
        val mediaFile = File(
            context.cacheDir,
            "photo_${System.currentTimeMillis()}.$it",
        )
        createdMediaFile =
            FileProvider.getUriForFile(context, context.packageName + ".provider", mediaFile)
        createdMediaFile!!
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            coroutineScope.launch {
                sheetState.hide()
                uri?.let {
                    viewModel.handleAction1Click(it)
                }
            }
        }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { _ ->
            coroutineScope.launch {
                sheetState.hide()
                createdMediaFile?.let {
                    viewModel.handleAction1Click(it)
                }
                createdMediaFile = null
            }
        }

    val captureVideoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) { _ ->
            coroutineScope.launch {
                sheetState.hide()
                createdMediaFile?.let { viewModel.handleAction1Click(it) }
                createdMediaFile = null
            }
        }

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        coroutineScope.launch {
            sheetState.hide()
            uri?.let { viewModel.handleAction1Click(it) }
        }
    }

    val microphonePermissionState = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )

    ProcessResultWarning(
        message = "Not başarılı bir şekilde kaydedildi",
        visible = uiState.showSaveNoteSuccessMessage
    )

    AddNewNoteScreenContent(
        modifier = modifier,
        noteStatus = uiState.noteStatus,
        titleValue = uiState.titleValue,
        descriptionValue = uiState.descriptionValue,
        selectedNoteType = uiState.selectedNoteType,
        selectedNoteTag = uiState.selectedNoteTag,
        isSaveReady = viewModel.isSaveReady(),
        sheetState = sheetState,
        selectedImages = uiState.selectedImages,
        selectedVideos = uiState.selectedVideos,
        selectedAudios = uiState.selectedAudios,
        isAudioRecording = uiState.audioRecordStatus == AudioRecordStatus.RECORDING,
        onEvent = onEvent,
        onAddMediaClick = {
            coroutineScope.launch {
                sheetState.show()
            }
        },
        action1Click = {
            when (uiState.selectedNoteType) {
                NoteType.IMAGE -> {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }

                NoteType.AUDIO -> {
                    audioPickerLauncher.launch("audio/*")
                }

                NoteType.VIDEO -> {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                    )
                }

                else -> {}
            }
        },
        action2Click = {
            when (uiState.selectedNoteType) {
                NoteType.IMAGE -> {
                    if (cameraPermissionState.allPermissionsGranted) {
                        takePictureLauncher.launch(createMediaFile("jpg"))
                    } else {
                        cameraPermissionState.launchMultiplePermissionRequest()
                    }
                }

                NoteType.AUDIO -> {
                    if (microphonePermissionState.status.isGranted) {
                        if (uiState.audioRecordStatus == AudioRecordStatus.RECORDING) {
                            coroutineScope.launch { sheetState.hide() }
                        }
                        onEvent(AddNewNoteUiEvent.OnRecordAudioClick)
                    } else {
                        microphonePermissionState.launchPermissionRequest()
                    }
                }

                NoteType.VIDEO -> {
                    if (cameraPermissionState.allPermissionsGranted) {
                        captureVideoLauncher.launch(createMediaFile(".mp4"))
                    } else {
                        cameraPermissionState.launchMultiplePermissionRequest()
                    }
                }

                else -> {}
            }
        },
        onNavigateUpClick = onNavigateUpClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddNewNoteScreenContent(
    modifier: Modifier = Modifier,
    noteStatus: NoteStatus,
    titleValue: String,
    descriptionValue: String,
    selectedNoteType: NoteType,
    selectedNoteTag: NoteTag,
    isSaveReady: Boolean,
    sheetState: SheetState,
    selectedImages: List<Uri>,
    selectedVideos: List<Uri>,
    selectedAudios: List<Uri>,
    isAudioRecording: Boolean,
    onEvent: (AddNewNoteUiEvent) -> Unit,
    onAddMediaClick: () -> Unit,
    action1Click: () -> Unit,
    action2Click: () -> Unit,
    onNavigateUpClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MNTopBar(
                title = if (noteStatus == NoteStatus.CREATE) "Create Note" else "Update Note",
                onNavigateBack = onNavigateUpClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .imePadding()
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
                Crossfade(targetState = selectedImages, label = "IMAGE") {
                    when (it) {
                        emptyList<Uri>() -> {
                            AddMediaContent(
                                text = "Click for the add Image",
                                icon = Icons.Default.Image,
                                onClick = onAddMediaClick
                            )
                        }

                        else -> {
                            AddMoreOrRemoveMedia(
                                mediaType = NoteType.IMAGE,
                                uri = selectedImages,
                                onAddMediaClick = onAddMediaClick,
                                onRemoveMediaClick = { index ->
                                    onEvent(
                                        AddNewNoteUiEvent.OnRemoveMediaClick(index)
                                    )
                                }
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(selectedNoteType == NoteType.AUDIO) {
                Crossfade(targetState = selectedAudios, label = "VIDEO") {
                    when (it) {
                        emptyList<Uri>() -> {
                            AddMediaContent(
                                text = "Click for the add audio",
                                icon = Icons.Default.AudioFile,
                                onClick = onAddMediaClick
                            )
                        }

                        else -> {
                            AddMoreOrRemoveMedia(
                                mediaType = NoteType.AUDIO,
                                uri = selectedAudios,
                                onAddMediaClick = onAddMediaClick,
                                onRemoveMediaClick = { index ->
                                    onEvent(
                                        AddNewNoteUiEvent.OnRemoveMediaClick(index)
                                    )
                                }
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(selectedNoteType == NoteType.VIDEO) {
                Crossfade(targetState = selectedVideos, label = "VIDEO") {
                    when (it) {
                        emptyList<Uri>() -> {
                            AddMediaContent(
                                text = "Click for the add video",
                                icon = Icons.Default.VideoFile,
                                onClick = onAddMediaClick
                            )
                        }

                        else -> {
                            AddMoreOrRemoveMedia(
                                mediaType = NoteType.VIDEO,
                                uri = selectedVideos,
                                onAddMediaClick = onAddMediaClick,
                                onRemoveMediaClick = { index ->
                                    onEvent(
                                        AddNewNoteUiEvent.OnRemoveMediaClick(index)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Button(
                    enabled = isSaveReady,
                    onClick = {
                        onEvent(
                            AddNewNoteUiEvent.OnSaveNoteClick(
                                context = context,
                                onNavBack = onNavigateUpClick
                            )
                        )
                    }) {
                    Text(text = if (noteStatus == NoteStatus.CREATE) "Save" else "Update")
                }
            }

            if (sheetState.currentValue == SheetValue.PartiallyExpanded || sheetState.currentValue == SheetValue.Expanded) {
                when (selectedNoteType) {
                    NoteType.IMAGE -> {
                        MediaBottomSheet(
                            action1Image = Icons.Default.Image,
                            action2Image = Icons.Default.CameraAlt,
                            action1Text = "From Gallery",
                            action2Text = "Capture Photo",
                            sheetState = sheetState,
                            action1OnClick = action1Click,
                            action2OnClick = action2Click
                        )
                    }

                    NoteType.AUDIO -> {
                        MediaBottomSheet(
                            action1Image = Icons.Default.AudioFile,
                            action2Image = Icons.Default.Mic,
                            action1Text = "From Device",
                            action2Text = "Record Audio",
                            sheetState = sheetState,
                            isAudioRecording = isAudioRecording,
                            onSaveAudioClick = { onEvent(AddNewNoteUiEvent.OnRecordAudioClick) },
                            action1OnClick = action1Click,
                            action2OnClick = action2Click
                        )
                    }

                    NoteType.VIDEO -> {
                        MediaBottomSheet(
                            action1Image = Icons.Default.VideoFile,
                            action2Image = Icons.Default.Videocam,
                            action1Text = "From Gallery",
                            action2Text = "Record Video",
                            sheetState = sheetState,
                            action1OnClick = action1Click,
                            action2OnClick = action2Click
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
            .height(144.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color.Black,
                    style = Stroke(
                        width = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
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

@Composable
private fun AddMoreOrRemoveMedia(
    mediaType: NoteType,
    uri: List<Uri>,
    onAddMediaClick: () -> Unit,
    onRemoveMediaClick: (Int) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(168.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .padding(end = 4.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier.background(
                    color = Color(0x80FFFFFF),
                    shape = RoundedCornerShape(25)
                )
            ) {
                Icon(
                    modifier = Modifier
                        .padding(2.dp)
                        .clickable(onClick = onAddMediaClick),
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(8.dp)
        ) {
            itemsIndexed(uri) { index, content ->
                Card {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        when (mediaType) {
                            NoteType.IMAGE -> {
                                AsyncImage(
                                    modifier = Modifier.fillMaxHeight(),
                                    model = content,
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit
                                )
                            }

                            NoteType.VIDEO -> VideoItem(content, context)
                            NoteType.AUDIO -> AudioItem(content, context)
                            else -> {}
                        }
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp, end = 4.dp)
                                .background(
                                    color = Color(0x80FFFFFF),
                                    shape = RoundedCornerShape(25)
                                )
                        ) {
                            Icon(
                                modifier = Modifier.clickable(onClick = { onRemoveMediaClick(index) }),
                                imageVector = Icons.Default.Remove,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AudioItem(content: Uri, context: Context) {
    val audioDuration = remember(content) {
        getAudioDuration(context, content)
    }
    val minute = if (audioDuration.first.length == 1) {
        "0${audioDuration.first}"
    } else audioDuration.first
    val second = if (audioDuration.second.length == 1) {
        "0${audioDuration.second}"
    } else audioDuration.second

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            imageVector = Icons.Default.AudioFile,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = "$minute:$second",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.W600
        )
    }
}

@Composable
private fun VideoItem(content: Uri, context: Context) {
    val bitmap = remember(content) {
        getVideoThumbnail(context, content)
    }
    bitmap?.let {
        Image(
            modifier = Modifier.fillMaxHeight(),
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun PreviewAddNewNoteScreen() {
    AddNewNoteScreenContent(
        titleValue = "",
        noteStatus = NoteStatus.CREATE,
        descriptionValue = "",
        selectedNoteType = NoteType.TEXT,
        selectedNoteTag = NoteTag.NONE,
        onEvent = {},
        onAddMediaClick = {},
        isSaveReady = false,
        selectedImages = emptyList(),
        selectedAudios = emptyList(),
        selectedVideos = emptyList(),
        isAudioRecording = false,
        sheetState = rememberStandardBottomSheetState(),
        action1Click = {},
        action2Click = {},
        onNavigateUpClick = {}
    )
}
