package com.ahmetocak.multinote.core.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

val PlayerHeight = 80.dp
private val PlayerShape = RoundedCornerShape(25)
private val PlayStopButtonSize = 56.dp

@Composable
fun AudioPlayer(
    modifier: Modifier = Modifier,
    isAudioPlaying: Boolean,
    currentAudioPosition: Int,
    duration: Pair<String, String>,
    increaseCurrentAudioPosition: () -> Unit,
    resetCurrentPosition: () -> Unit,
    onPlayButtonClicked: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(PlayerHeight),
        shape = PlayerShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(8f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerButton(
                    modifier = Modifier.size(PlayStopButtonSize),
                    onPlayButtonClicked = onPlayButtonClicked,
                    isAudioPlaying = isAudioPlaying
                )
                MusicSlider(
                    isAudioPlaying = isAudioPlaying,
                    audioDuration = duration,
                    currentAudioPosition = currentAudioPosition,
                    increaseCurrentAudioPosition = increaseCurrentAudioPosition,
                    resetCurrentPosition = resetCurrentPosition
                )
            }
        }
    }
}

@Composable
private fun PlayerButton(
    modifier: Modifier,
    isAudioPlaying: Boolean,
    onPlayButtonClicked: () -> Unit
) {
    IconButton(onClick = onPlayButtonClicked) {
        Icon(
            modifier = modifier,
            imageVector = if (isAudioPlaying) {
                Icons.Default.Stop
            } else {
                Icons.Default.PlayArrow
            },
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MusicSlider(
    isAudioPlaying: Boolean,
    audioDuration: Pair<String, String>,
    currentAudioPosition: Int,
    increaseCurrentAudioPosition: () -> Unit,
    resetCurrentPosition: () -> Unit
) {
    LaunchedEffect(isAudioPlaying) {
        val duration = (audioDuration.first.toInt() * 60) + audioDuration.second.toInt()
        if (isAudioPlaying && currentAudioPosition < duration) {
            while (true) {
                delay(1000)
                increaseCurrentAudioPosition()
            }
        } else if (isAudioPlaying) {
            resetCurrentPosition()
            while (true) {
                delay(1000)
                increaseCurrentAudioPosition()
            }
        }
    }

    Column {
        Slider(
            value = currentAudioPosition.toFloat(),
            onValueChange = {},
            valueRange = 0f..audioDuration.second.toFloat() + (audioDuration.first.toFloat() * 60),
            colors = SliderDefaults.colors(
                inactiveTrackColor = Color.Unspecified.copy(alpha = 0.3f)
            ),
            thumb = {},
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    thumbTrackGapSize = 0.dp
                )
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.W400
            )
            /*
            val secondDur = audioDuration.second.toInt()
            val secondDurText = if (secondDur < 60) {
                if (secondDur < 10) {
                    "00:0$secondDur"
                } else {
                    "00:$secondDur"
                }
            } else {
                val minute = secondDur / 60
                if (minute < 10) {
                    if (secondDur % 60 < 10) {
                        "0$minute:0${secondDur % 60}"
                    } else {
                        "0$minute:${secondDur % 60}"
                    }
                } else {
                    if (secondDur % 60 < 10) {
                        "$minute:0${secondDur % 60}"
                    } else {
                        "$minute:${secondDur % 60}"
                    }
                }
            }
             */
            val minute = audioDuration.first.toInt()
            val second = audioDuration.second.toInt()

            val minuteText = if (minute < 10) {
                "0$minute"
            } else {
                "$minute"
            }
            val secondText = if (second < 10) {
                "0$second"
            } else {
                "$second"
            }

            Text(
                text = if (currentAudioPosition < 10) {
                    "00:0$currentAudioPosition"
                } else {
                    val minute = currentAudioPosition / 60
                    if (minute < 10) {
                        if (currentAudioPosition % 60 < 10) {
                            "0$minute:0${currentAudioPosition % 60}"
                        } else {
                            "0$minute:${currentAudioPosition % 60}"
                        }
                    } else {
                        if (currentAudioPosition % 60 < 10) {
                            "minute:0${currentAudioPosition % 60}"
                        } else {
                            "minute:${currentAudioPosition % 60}"
                        }
                    }
                },
                style = style
            )
            Text(
                text = "$minuteText:$secondText",
                style = style
            )
        }
    }
}
/*
@Composable
@Preview(showSystemUi = true)
private fun PlayerPreview() {
    AudioPlayer(
        audioDuration = 235,
        onPlayButtonClicked = {},
        isAudioPlaying = false,
        currentAudioPosition = 0,
        increaseCurrentAudioPosition = {},
    )
}

@Composable
@Preview(showSystemUi = true)
private fun PlayerPlayingPreview() {
    AudioPlayer(
        onPlayButtonClicked = {},
        isAudioPlaying = true,
        audioDuration = 100,
        currentAudioPosition = 25,
        increaseCurrentAudioPosition = {}
    )
}

 */