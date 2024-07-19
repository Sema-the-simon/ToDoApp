package com.example.todoapp.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todoapp.ui.animations.enterSlideInto
import com.example.todoapp.ui.animations.exitSlideOut
import com.example.todoapp.ui.screens.settings.SettingsScreen
import com.example.todoapp.ui.screens.settings.SettingsViewModel

fun NavGraphBuilder.settingsDestination(
    onNavigateUp: () -> Unit,
    navigateToInfo: () -> Unit
) {
    composable<Destination.Settings>(
        enterTransition = { enterSlideInto() },
        exitTransition = { exitSlideOut() }
    ) {
        val settingsViewModel: SettingsViewModel = hiltViewModel()
        val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
        SettingsScreen(
            uiState = settingsUiState,
            onUiAction = settingsViewModel::onUiAction,
            navigateBack = onNavigateUp,
            navigateToInfo = navigateToInfo
        )

        BackHandler {
            onNavigateUp()
        }
    }
}