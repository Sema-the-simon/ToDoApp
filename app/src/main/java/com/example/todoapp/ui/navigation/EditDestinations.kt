package com.example.todoapp.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todoapp.ui.screens.edit.EditScreen
import com.example.todoapp.ui.screens.edit.EditViewModel

fun NavGraphBuilder.editDestinations(
    onNavigateUp: () -> Unit
) {
    composable<Destination.Edit> {
        val editViewModel: EditViewModel = hiltViewModel()
        val editUiState by editViewModel.uiState.collectAsStateWithLifecycle()
        EditScreen(
            uiState = editUiState,
            onUiAction = editViewModel::onUiAction,
            navigateUp = onNavigateUp
        )
    }
}