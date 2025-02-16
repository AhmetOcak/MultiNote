package com.ahmetocak.multinote.features.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.multinote.R
import com.ahmetocak.multinote.core.ui.theme.color_schemes.CustomColorScheme
import com.ahmetocak.multinote.core.ui.theme.color_schemes.NeonColorScheme
import com.ahmetocak.multinote.features.settings.components.ColorSchemesSettingItem
import com.ahmetocak.multinote.features.settings.components.SettingItem
import com.ahmetocak.multinote.features.settings.components.SettingSection
import com.ahmetocak.multinote.model.ColorSchemeKeys

private const val APP_URL = ""

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkThemeChecked: Boolean,
    isDynamicColorChecked: Boolean,
    currentScheme: CustomColorScheme,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.settings))
            })
        },
        bottomBar = {
            // TODO: BOTTOM BAR
        }
    ) { paddingValues ->
        SettingsScreenContent(
            modifier = Modifier.padding(paddingValues),
            isDarkThemeChecked = isDarkThemeChecked,
            onDarkThemeCheckedChange = viewModel::updateDarkTheme,
            isDynamicColorChecked = isDynamicColorChecked,
            onDynamicColorCheckedChange = viewModel::updateDynamicColor,
            onColorSchemeClick = viewModel::updateColorScheme,
            currentScheme = currentScheme,
            onRateAppClick = remember {
                {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(APP_URL)
                    context.startActivity(intent)
                }
            },
            onShareAppClick = remember {
                {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, APP_URL)
                    }
                    context.startActivity(
                        Intent.createChooser(
                            intent,
                            context.getString(R.string.share_app)
                        )
                    )
                }
            }
        )
    }
}

@Composable
private fun SettingsScreenContent(
    modifier: Modifier,
    isDarkThemeChecked: Boolean,
    onDarkThemeCheckedChange: (Boolean) -> Unit,
    isDynamicColorChecked: Boolean,
    onDynamicColorCheckedChange: (Boolean) -> Unit,
    onColorSchemeClick: (ColorSchemeKeys) -> Unit,
    currentScheme: CustomColorScheme,
    onRateAppClick: () -> Unit,
    onShareAppClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SettingSection(title = stringResource(id = R.string.themes)) {
            SettingItem(
                nameId = Settings.DARK_THEME.nameId,
                icon = Settings.DARK_THEME.icon,
                checked = isDarkThemeChecked,
                onCheckedChange = onDarkThemeCheckedChange
            )
            ColorSchemesSettingItem(
                nameId = Settings.COLOR_THEMES.nameId,
                icon = Settings.COLOR_THEMES.icon,
                onClick = onColorSchemeClick,
                currentScheme = currentScheme,
                isDarkTheme = isDarkThemeChecked
            )
            if (Build.VERSION.SDK_INT >= 31) {
                SettingItem(
                    nameId = Settings.DYNAMIC_COLOR.nameId,
                    icon = Settings.DYNAMIC_COLOR.icon,
                    checked = isDynamicColorChecked,
                    onCheckedChange = onDynamicColorCheckedChange
                )
            }
        }
        SettingSection(title = stringResource(id = R.string.app_name)) {
            SettingItem(
                nameId = Settings.RATE_APP.nameId,
                icon = Settings.RATE_APP.icon,
                onClick = onRateAppClick
            )
            SettingItem(
                nameId = Settings.SHARE_APP.nameId,
                icon = Settings.SHARE_APP.icon,
                onClick = onShareAppClick
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun PreviewSettingsContent() {
    SettingsScreenContent(
        modifier = Modifier,
        isDynamicColorChecked = false,
        onDarkThemeCheckedChange = {},
        isDarkThemeChecked = false,
        onColorSchemeClick = {},
        onRateAppClick = {},
        currentScheme = NeonColorScheme,
        onDynamicColorCheckedChange = {},
        onShareAppClick = {}
    )
}