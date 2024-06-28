package com.example.todoapp.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todoapp.ui.screens.edit.EditScreen
import com.example.todoapp.ui.screens.edit.EditViewModel
import com.example.todoapp.ui.screens.edit.action.EditUiEvent

fun NavGraphBuilder.editDestinations(
    onNavigateUp: () -> Unit
) {
    composable<Destination.Edit> {
        val editViewModel: EditViewModel = hiltViewModel()
        val editUiState by editViewModel.uiState.collectAsStateWithLifecycle()
        val editUiEvent by editViewModel.uiEvent.collectAsStateWithLifecycle(null)
        EditScreen(
            uiState = editUiState,
            uiEvent = editUiEvent,
            onUiAction = editViewModel::onUiAction,
            navigateUp = onNavigateUp
        )
    }
}