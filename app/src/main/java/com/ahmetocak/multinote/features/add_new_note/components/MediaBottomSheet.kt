package com.ahmetocak.multinote.features.add_new_note.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaBottomSheet(
    action1Image: ImageVector,
    action2Image: ImageVector,
    action1Text: String,
    action2Text: String,
    sheetState: SheetState,
    isAudioRecording: Boolean = false,
    onSaveAudioClick: () -> Unit = {},
    action1OnClick: () -> Unit,
    action2OnClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = Modifier.navigationBarsPadding(),
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
            }
        },
        sheetState = sheetState
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                }
            ) {
                Text(text = "cancel")
            }
        }
        Crossfade(targetState = isAudioRecording) {
            if (it) {
                val infiniteTransition = rememberInfiniteTransition(label = "recording text")
                val animatedColor by infiniteTransition.animateColor(
                    initialValue = Color(0xFFFF3131),
                    targetValue = Color(0xFFC41E3A),
                    animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
                    label = "color"
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        imageVector = Icons.Filled.Mic,
                        contentDescription = null,
                        tint = animatedColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "recording",
                        style = MaterialTheme.typography.bodyLarge,
                        color = animatedColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    IconButton(onClick = onSaveAudioClick) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Filled.Stop,
                            contentDescription = null
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Action(icon = action1Image, text = action1Text, onClick = action1OnClick)
                    Action(icon = action2Image, text = action2Text, onClick = action2OnClick)
                }
            }
        }
    }
}

@Composable
private fun Action(icon: ImageVector, text: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(25)
        ) {
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp),
                imageVector = icon,
                contentDescription = null
            )
        }
        Text(text = text)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun PreviewMediaBottomSheet() {
    MediaBottomSheet(
        action1Image = Icons.Default.Image,
        action2Image = Icons.Default.CameraAlt,
        action1Text = "Pick up from gallery",
        action2Text = "Use camera",
        sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded),
        isAudioRecording = false,
        action1OnClick = {},
        action2OnClick = {},
    )
}