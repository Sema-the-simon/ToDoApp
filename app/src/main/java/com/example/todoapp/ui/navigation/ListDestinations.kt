package com.example.todoapp.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todoapp.ui.screens.list.ListScreen
import com.example.todoapp.ui.screens.list.ListViewModel

fun NavGraphBuilder.listDestinations(
    onNavigateToItem: (todoItemId: String) -> Unit
) {
    composable<Destination.List> {
        val listViewModel: ListViewModel = hiltViewModel()
        val listUiState by listViewModel.uiState.collectAsStateWithLifecycle()
        ListScreen(
            uiState = listUiState,
            onUiAction = listViewModel::onUiAction,
            navigateToEditItem = onNavigateToItem
        )
    }
}