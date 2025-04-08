package com.ahmetocak.multinote.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MNTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            if (title != null)
                Text(text = title)
        },
        navigationIcon = {
            if (onNavigateBack != null) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null)
                }
            }
        },
        actions = actions
    )
}