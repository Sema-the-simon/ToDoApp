package com.example.todoapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

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
            startDestination = Destination.List
        ) {
            listDestinations { todoItemId ->
                navController.navigate(Destination.Edit(todoItemId))
            }

            editDestinations {
                navController.navigate(Destination.List) {
                    popUpTo(Destination.List) {
                        inclusive = true
                    }
                }
            }
        }
    }
}