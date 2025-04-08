package com.ahmetocak.multinote.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun ProcessResultWarning(message: String, visible: Boolean = true) {
    val width = LocalConfiguration.current.screenWidthDp.dp / 2

    AnimatedVisibility(modifier = Modifier.zIndex(1f), visible = visible) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .statusBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .width(width)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = message,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}