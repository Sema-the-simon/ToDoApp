package com.example.todoapp.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todoapp.ui.animations.enterSlideInto
import com.example.todoapp.ui.animations.exitSlideOut
import com.example.todoapp.ui.screens.edit.EditScreen
import com.example.todoapp.ui.screens.edit.EditViewModel

fun NavGraphBuilder.editDestination(
    onNavigateUp: () -> Unit
) {
    composable<Destination.Edit>(
        enterTransition = {
            enterSlideInto()
        },
        exitTransition = { exitSlideOut() }
    ) {
        val editViewModel: EditViewModel = hiltViewModel()
        val editUiState by editViewModel.uiState.collectAsStateWithLifecycle()
        val editUiEvent by editViewModel.uiEvent.collectAsStateWithLifecycle(null)

        EditScreen(
            uiState = editUiState,
            uiEvent = editUiEvent,
            onUiAction = editViewModel::onUiAction,
            navigateUp = onNavigateUp
        )
        BackHandler {
            onNavigateUp()
        }
    }
}