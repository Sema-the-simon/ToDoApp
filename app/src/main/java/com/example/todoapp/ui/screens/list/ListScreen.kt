package com.example.todoapp.ui.screens.list

import android.content.Context
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.ui.screens.list.action.ListUiAction
import com.example.todoapp.ui.screens.list.components.CountdownSnackbar
import com.example.todoapp.ui.screens.list.components.ListTopAppBar
import com.example.todoapp.ui.screens.list.components.TodoList
import com.example.todoapp.ui.themes.Blue
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme
import com.example.todoapp.ui.themes.White
import com.example.todoapp.utils.getData
import kotlinx.coroutines.launch

/**
 * `ListScreen` displays the user interface for viewing and interacting with a list of todo items.
 *
 * This screen includes:
 * - **`[ListTopAppBar]`**: A top app bar with options for filtering tasks and showing the count of completed tasks.
 * - **`[FloatingActionButton]`**: A button for adding new todo items.
 * - **`[TodoList]`**: A scrollable list of todo items with options to interact with each item.
 * - Pull-to-refresh functionality to reload the list of todo items.
 * - Snackbar notifications for displaying error messages or other notifications.
 **/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    uiState: ListUiState,
    onUiAction: (ListUiAction) -> Unit,
    navigateToEditItem: (String?) -> Unit,
    navigateToSettings: () -> Unit
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

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val undoSnackBarStack = uiState.undoElementsStack

    LaunchedErrorSnackbar(uiState, snackbarHostState, context, onUiAction, undoSnackBarStack)



    LaunchedEffect(undoSnackBarStack.size) {
        if (undoSnackBarStack.isNotEmpty()) {
            launch {
                val currentTodo = undoSnackBarStack.last()
                val res = snackbarHostState.showSnackbar(
                    message = "Delete task: ${currentTodo.text}",
                    actionLabel = "Cancel",
                    withDismissAction = true,
                    duration = SnackbarDuration.Indefinite
                )
                when (res) {
                    SnackbarResult.ActionPerformed -> {
                        onUiAction(ListUiAction.ShowHiddenTodoItem(currentTodo.id))
                        onUiAction(ListUiAction.RemoveLastInUndoStack)
                    }

                    SnackbarResult.Dismissed -> {
                        onUiAction(ListUiAction.RemoveTodoItems(undoSnackBarStack.map { todo -> todo.id }))
                        onUiAction(ListUiAction.ClearUndoStack)
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                if (data.visuals.withDismissAction) {
                    CountdownSnackbar(data = data)
                } else
                    Snackbar(snackbarData = data)

            }
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
                },
                navigateToSettings = navigateToSettings,

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
            TodoList(
                listState = listState,
                todoList = uiState.todoItems,
                isDataSynchronized = uiState.isDataSynchronized,
                onUpdateItem = { id -> onUiAction(ListUiAction.UpdateTodoItem(id)) },
                onDeleteItem = { todo ->
                    onUiAction(ListUiAction.HideTodoItem(todo.id))
                    snackbarHostState.currentSnackbarData?.let { data ->
                        if (!data.visuals.withDismissAction)
                            data.dismiss()
                    }
                    onUiAction(ListUiAction.AddInUndoStack(todo))
                },
                onItemClick = { todoItemId -> navigateToEditItem(todoItemId) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun LaunchedErrorSnackbar(
    uiState: ListUiState,
    snackbarHostState: SnackbarHostState,
    context: Context,
    onUiAction: (ListUiAction) -> Unit,
    undoStack: List<TodoItem>
) {
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            if (undoStack.isEmpty())
                launch {
                    snackbarHostState.showSnackbar(context.getString(message.toStringResource()))
                    onUiAction(ListUiAction.ClearErrorMessage)
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
        ListScreen(ListUiState(getData().take(2), 1), {}, {}, {})
    }
}