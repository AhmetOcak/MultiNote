package com.ahmetocak.multinote.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberAppNavController(
    navController: NavHostController = rememberNavController()
): AppNavController = remember(navController) {
    AppNavController(navController)
}

@Stable
class AppNavController(val navController: NavHostController) {

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateNote(noteId: Int, from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate("${Destinations.NOTE_ROUTE}/$noteId")
        }
    }

    fun navigateSettings(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(Destinations.SETTINGS_ROUTE)
        }
    }

    fun navigateCreateNote(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(Destinations.CREATE_NOTE_ROUTE)
        }
    }
}

private fun shouldNavigate(from: NavBackStackEntry): Boolean = from.isLifecycleResumed()

private fun NavBackStackEntry.isLifecycleResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED