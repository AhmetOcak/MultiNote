package com.ahmetocak.multinote.features.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayer(videoPath: String, viewModel: NoteViewModel) {
    val context = LocalContext.current
    val player by viewModel.playerState.collectAsStateWithLifecycle()

    LaunchedEffect(videoPath) {
        viewModel.initializePlayer(context, videoPath)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.savePlayerState()
            viewModel.releasePlayer()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                IconButton(onClick = viewModel::onBackClickedInVideoState) {
                    Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = null)
                }
            }
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    PlayerView(context).apply {
                        this.player = player
                    }
                },
                update = { playerView ->
                    playerView.player = player
                }
            )
        }
    }
}
/*
@Composable
private fun PlayerControls(player: ExoPlayer?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { player?.playWhenReady = true }) {
            Text("Play")
        }
        Button(onClick = { player?.playWhenReady = false }) {
            Text("Pause")
        }

        Button(onClick = {
            player?.seekTo(player.currentPosition - 10_000) // Seek backward 10 seconds
        }) {
            Text("Seek -10s")
        }
        Button(onClick = {
            player?.seekTo(player.currentPosition + 10_000) // Seek forward 10 seconds
        }) {
            Text("Seek +10s")
        }
    }
}
 */