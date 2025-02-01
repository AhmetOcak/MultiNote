package com.ahmetocak.multinote.features.add_new_note.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaBottomSheet(
    action1Image: ImageVector,
    action2Image: ImageVector,
    action1OnClick: () -> Unit,
    action2OnClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)

    ModalBottomSheet(
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
            TextButton(onClick = { coroutineScope.launch { sheetState.hide() } }) {
                Text(text = "cancel")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Action(icon = action1Image, onClick = action1OnClick)
            Action(icon = action2Image, onClick = action2OnClick)
        }
    }
}

@Composable
private fun Action(icon: ImageVector, onClick: () -> Unit) {
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
}


@Preview(showSystemUi = true)
@Composable
private fun PreviewMediaBottomSheet() {
    MediaBottomSheet(
        action1Image = Icons.Default.Image,
        action2Image = Icons.Default.CameraAlt,
        action1OnClick = {},
        action2OnClick = {}
    )
}