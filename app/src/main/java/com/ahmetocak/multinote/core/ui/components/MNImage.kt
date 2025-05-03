package com.ahmetocak.multinote.core.ui.components

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun MNImage(
    modifier: Modifier = Modifier,
    imagePath: String?,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(imagePath)
            .build(),
        contentDescription = null,
        contentScale = contentScale
    )
}

@Composable
fun MNZoomableImage(
    modifier: Modifier = Modifier,
    imagePath: String?,
    contentScale: ContentScale = ContentScale.Crop
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .transformable(state = state)
            .graphicsLayer(
                scaleX = scale.coerceIn(1f, 5f),
                scaleY = scale.coerceIn(1f, 5f),
                translationX = offset.x,
                translationY = offset.y
            )
    ) {
        AsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(LocalContext.current)
                .data(imagePath)
                .build(),
            contentDescription = null,
            contentScale = contentScale
        )
    }
}
