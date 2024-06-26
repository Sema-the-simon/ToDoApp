package com.example.todoapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.navigation.Edit
import com.example.todoapp.ui.navigation.List
import com.example.todoapp.ui.screens.edit.EditScreen
import com.example.todoapp.ui.screens.edit.EditViewModel
import com.example.todoapp.ui.screens.list.ListScreen
import com.example.todoapp.ui.screens.list.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(
                    modifier = Modifier,
                    navController = navController,
                    startDestination = List.route
                ) {

                    composable(List.route) {
                        val listViewModel: ListViewModel = hiltViewModel()
                        val listUiState by listViewModel.uiState.collectAsState()
                        ListScreen(
                            uiState = listUiState,
                            onUiAction = listViewModel::onUiAction,
                            navigateToNewItem = { navController.navigate(Edit.route) },
                            navigateToEditItem = { id -> navController.navigate(Edit.navToEditWithArgs(id)) }
                        )
                    }

                    composable(Edit.route, arguments = Edit.arguments) {
                        val editViewModel: EditViewModel = hiltViewModel()
                        val editUiState by editViewModel.uiState.collectAsState()
                        EditScreen(
                            uiState = editUiState,
                            onUiAction = editViewModel::onUiAction,
                            navigateUp = {
                                navController.navigate(List.route) {
                                    popUpTo(List.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}