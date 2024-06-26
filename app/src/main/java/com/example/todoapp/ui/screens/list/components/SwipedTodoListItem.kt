package com.example.todoapp.ui.screens.list.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipedTodoListItem(
    todoItem: TodoItem,
    onCheckboxClick: () -> Unit,
    onItemClick: () -> Unit,
    onDeleteSwipe: () -> Unit,
    onUpdateSwipe: () -> Unit,
    modifier: Modifier = Modifier
) {
    var show by remember { mutableStateOf(true) }
    val dismissState = rememberSwipeToDismissBoxState(
        initialValue = if (show)
            SwipeToDismissBoxValue.Settled
        else
            SwipeToDismissBoxValue.EndToStart,
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.Settled -> false
                SwipeToDismissBoxValue.StartToEnd -> {
                    onUpdateSwipe()
                    false
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    show = false
                    true
                }

            }
        },
        positionalThreshold = { 150f }
    )
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            DismissBackground(dismissState = dismissState, isTaskDone = todoItem.isDone)
        },
        content = {
            TodoListItem(
                todoItem = todoItem,
                onCheckboxClick = onCheckboxClick,
                onItemClick = onItemClick
            )
        }
    )

    LaunchedEffect(show) {
        if (!show) {
            delay(100)
            onDeleteSwipe()
            show = true
        }
    }
}