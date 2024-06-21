package com.example.todoapp.ui.screens.list

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.ui.screens.list.action.ListUiAction
import com.example.todoapp.ui.screens.list.components.ListTopAppBar
import com.example.todoapp.ui.screens.list.components.TodoList
import com.example.todoapp.ui.themes.Blue
import com.example.todoapp.ui.themes.LightBackPrimary
import com.example.todoapp.ui.themes.White
import com.example.todoapp.utils.getData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    uiState: ListUiState,
    onUiAction: (ListUiAction) -> Unit,
    navigateToNewItem: () -> Unit,
    navigateToEditItem: (String) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()

    val isTopScroll by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    var needToScroll by remember { mutableStateOf(false) }

    LaunchedEffect(needToScroll && !isTopScroll) {
        launch {
            listState.animateScrollToItem(index = 0)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ListTopAppBar(
                scrollBehavior = scrollBehavior,
                doneTasks = uiState.countDoneTasks,
                isFiltered = uiState.isFiltered,
                onVisibilityClick = { isFiltered ->
                    onUiAction(ListUiAction.ChangeFilter(isFiltered))
                    if (isTopScroll && !isFiltered) {
                        needToScroll = true
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToNewItem() },
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
                onItemClick = { todoItem -> navigateToEditItem(todoItem.id) },
                onDelete = { todoItem -> onUiAction(ListUiAction.RemoveTodoItem(todoItem)) },
                onUpdate = { todoItem ->
                    onUiAction(
                        ListUiAction.UpdateTodoItem(
                            todoItem.copy(
                                isDone = !todoItem.isDone
                            )
                        )
                    )
                },
                listState = listState
            )

        }
    }


}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewListScreen() {
    ListScreen(ListUiState(getData(), 1), {}, {}, {})
}