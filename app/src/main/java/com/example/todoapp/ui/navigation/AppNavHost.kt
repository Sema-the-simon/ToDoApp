package com.example.todoapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

/** Composable function for setting up the main app's navigation graph. */

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = Destination.List()
        ) {

            listDestination(
                onNavigateToItem = { todoItemId ->
                    navController.navigate(Destination.Edit(todoItemId))
                },
                onNavigateToSettings = {
                    navController.navigate(Destination.Settings)
                }
            )

            editDestination { hideElementId ->
                navController.navigate(Destination.List(hideElementId)) {
                    popUpTo(Destination.List()) {
                        inclusive = true
                    }
                }
            }
            settingsDestination(
                onNavigateUp = {
                    navController.navigate(Destination.List()) {
                        popUpTo(Destination.List()) {
                            inclusive = true
                        }
                    }
                },
                navigateToInfo = {
                    navController.navigate(Destination.Info)
                }
            )

            infoDestination {
                navController.navigate(Destination.Settings) {
                    popUpTo(Destination.Settings) {
                        inclusive = true
                    }
                }
            }
        }
    }
}