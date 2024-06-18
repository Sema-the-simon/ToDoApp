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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.navigation.Edit
import com.example.todoapp.ui.navigation.List
import com.example.todoapp.ui.screens.edit.EditScreen
import com.example.todoapp.ui.screens.edit.EditViewModel
import com.example.todoapp.ui.screens.list.ListScreen
import com.example.todoapp.ui.screens.list.ListViewModel

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
                    startDestination = Edit.route
                ) {

                    composable(List.route) {
                        val listViewModel : ListViewModel = viewModel()
                        val listUiState by listViewModel.uiState.collectAsState()
                        ListScreen(
                            navController = navController,
                            uiState = listUiState,
                            onUiAction = listViewModel::onUiAction,
                            navigateToNewItem = {},
                            navigateToEditItem = {}
                        )
                    }

                    composable(Edit.route) {
                        val editViewModel : EditViewModel = viewModel()
                        val editUiState by editViewModel.uiState.collectAsState()
                        EditScreen(
                            navController = navController,
                            uiState = editUiState,
                            onUiAction = editViewModel ::onUiAction,
                            navigateUp = {}
                        )
                    }
                }
            }
        }
    }
}