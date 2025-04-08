package com.ahmetocak.multinote.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ahmetocak.multinote.BracketShortCuts
import com.ahmetocak.multinote.CharacterEffects
import com.ahmetocak.multinote.KeyboardShortCuts

@Composable
fun NoteShortcut(
    onKeyboardShortCutsClick: (KeyboardShortCuts) -> Unit,
    onBracketShortCutsClick: (BracketShortCuts) -> Unit,
    onCharacterEffectsClick: (CharacterEffects) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BracketShortCuts.entries.forEach {
                        ShortCutItem(
                            text = it.toString(),
                            onClick = { onBracketShortCutsClick(it) }
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CharacterEffects.entries.forEach {
                        ShortCutItem(
                            text = it.toString(),
                            onClick = { onCharacterEffectsClick(it) }
                        )
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth(), Alignment.Center) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KeyboardShortCuts.entries.forEach {
                        ShortCutItem(
                            text = it.toString(),
                            onClick = { onKeyboardShortCutsClick(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShortCutItem(text: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.size(36.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontWeight = FontWeight.W500,
                textAlign = TextAlign.Center
            )
        }
    }
}