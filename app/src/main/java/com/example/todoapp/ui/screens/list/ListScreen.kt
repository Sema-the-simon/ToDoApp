package com.example.todoapp.ui.screens.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.screens.list.action.ListUiAction
import com.example.todoapp.ui.screens.list.components.ListTopAppBar
import com.example.todoapp.ui.screens.list.components.TodoList
import com.example.todoapp.ui.themes.Blue
import com.example.todoapp.ui.themes.LightBackPrimary
import com.example.todoapp.ui.themes.White
import com.example.todoapp.utils.getData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navController: NavHostController,
    uiState: ListUiState,
    onUiAction: (ListUiAction) -> Unit,
    navigateToNewItem: () -> Unit,
    navigateToEditItem: () -> Unit
) {
//    val viewModel: ListViewModel = viewModel()
//    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ListTopAppBar(
                scrollBehavior = scrollBehavior,
                doneTasks = uiState.countDoneTasks,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToNewItem() },//navigation ADD NEW
                shape = CircleShape,
                containerColor = Blue,
                contentColor = White
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
            }
        },
        containerColor = LightBackPrimary,
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TodoList(
                todoList = uiState.todoItems,
                onItemClick = { todoItem -> navigateToEditItem() },//navigation EDIT
                onDelete = { todoItem -> onUiAction(ListUiAction.RemoveTodoItem(todoItem)) },
                onUpdate = { todoItem ->
                    onUiAction(
                        ListUiAction.UpdateTodoItem(
                            todoItem.copy(
                                isDone = !todoItem.isDone
                            )
                        )
                    )
                }
            )
        }
    }


}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewListScreen() {
    ListScreen(rememberNavController(), ListUiState(getData(), 1), {}, {}, {})
}