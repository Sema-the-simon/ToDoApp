package com.example.todoapp.ui.screens.list

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.todoapp.ui.screens.list.action.ListUiAction
import com.example.todoapp.ui.screens.list.components.ListTopAppBar
import com.example.todoapp.ui.screens.list.components.TodoList
import com.example.todoapp.ui.themes.Blue
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme
import com.example.todoapp.ui.themes.White
import com.example.todoapp.utils.getData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    uiState: ListUiState,
    onUiAction: (ListUiAction) -> Unit,
    navigateToEditItem: (String?) -> Unit
) {

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val listState = rememberLazyListState()
    var needToScroll by remember { mutableStateOf(false) }
    val isTopScroll by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    LaunchedEffect(needToScroll && !isTopScroll) {
        launch {
            listState.animateScrollToItem(index = 0)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            launch {
                snackbarHostState.showSnackbar(context.getString(uiState.errorMessage.toStringResource()))
                onUiAction(ListUiAction.ClearErrorMessage)
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
                onClick = { navigateToEditItem(null) },
                shape = CircleShape,
                containerColor = Blue,
                contentColor = White
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
            }
        },
        containerColor = ExtendedTheme.colors.backPrimary,
    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier.padding(paddingValues),
            isRefreshing = uiState.isRefreshing,
            onRefresh = { onUiAction(ListUiAction.RefreshList) }
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .scrollable(rememberLazyListState(), orientation = Orientation.Vertical)
            ) {
                TodoList(
                    listState = listState,
                    todoList = uiState.todoItems,
                    isDataSynchronized = uiState.isDataSynchronized,
                    onUpdate = { todoItem -> onUiAction(ListUiAction.UpdateTodoItem(todoItem.id)) },
                    onItemClick = { todoItemId -> navigateToEditItem(todoItemId) },
                    onDelete = { todoItem -> onUiAction(ListUiAction.RemoveTodoItem(todoItem.id)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }


}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, locale = "ru")
@Composable
fun PreviewListScreen(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme(isDarkTheme) {
        ListScreen(ListUiState(getData().take(2), 1), {}, {})
    }
}