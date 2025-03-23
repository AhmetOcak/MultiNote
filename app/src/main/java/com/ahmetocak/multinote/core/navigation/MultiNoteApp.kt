package com.ahmetocak.multinote.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.ahmetocak.multinote.core.ui.theme.MultiNoteTheme
import com.ahmetocak.multinote.core.ui.theme.color_schemes.CustomColorScheme
import com.ahmetocak.multinote.features.add_new_note.AddNewNoteScreen
import com.ahmetocak.multinote.features.home.HomeScreen
import com.ahmetocak.multinote.features.note.NoteScreen
import com.ahmetocak.multinote.features.settings.SettingsScreen

@Composable
fun MultiNoteApp(
    isDarkThemeChecked: Boolean,
    isDynamicColorChecked: Boolean,
    currentScheme: CustomColorScheme
) {
    MultiNoteTheme(
        customColorScheme = currentScheme,
        darkTheme = isDarkThemeChecked,
        dynamicColor = isDynamicColorChecked
    ) {
        val appNavController = rememberAppNavController()

        Surface {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = appNavController.navController,
                startDestination = Destinations.HOME_ROUTE
            ) {
                navGraph(
                    isDarkThemeChecked = isDarkThemeChecked,
                    isDynamicColorChecked = isDynamicColorChecked,
                    currentScheme = currentScheme,
                    onCreateNoteClick = appNavController::navigateCreateNote,
                    onNavigateUpClick = appNavController::upPress
                )
            }
        }
    }
}

private fun NavGraphBuilder.navGraph(
    isDarkThemeChecked: Boolean,
    isDynamicColorChecked: Boolean,
    currentScheme: CustomColorScheme,
    onCreateNoteClick: (NavBackStackEntry) -> Unit,
    onNavigateUpClick: () -> Unit
) {
    composable(route = Destinations.HOME_ROUTE) {
        HomeScreen(onCreateNoteClick = { onCreateNoteClick(it) })
    }
    composable(route = Destinations.SETTINGS_ROUTE) {
        SettingsScreen(
            isDarkThemeChecked = isDarkThemeChecked,
            isDynamicColorChecked = isDynamicColorChecked,
            currentScheme = currentScheme
        )
    }
    composable(route = Destinations.CREATE_NOTE_ROUTE) {
        AddNewNoteScreen(onNavigateUpClick = onNavigateUpClick)
    }
    composable(
        route = "${Destinations.NOTE_ROUTE}/{${Arguments.NOTE_ID}}",
        arguments = listOf(
            navArgument(Arguments.NOTE_ID) { NavType.IntType }
        )
    ) {
        NoteScreen()
    }
}