package com.example.todoapp.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todoapp.ui.screens.settings.SettingsScreen
import com.example.todoapp.ui.screens.settings.SettingsViewModel

fun NavGraphBuilder.settingsDestination(
    onNavigateUp: () -> Unit
) {
    composable<Destination.Settings> {
        val settingsViewModel: SettingsViewModel = hiltViewModel()
        val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
        SettingsScreen(
            uiState = settingsUiState,
            onUiAction = settingsViewModel::onUiAction,
            navigateBack = onNavigateUp
        )
    }
}