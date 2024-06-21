package com.example.todoapp.ui.screens.list.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.themes.LightBackPrimary
import com.example.todoapp.utils.getData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoList(
    todoList: List<TodoItem>,
    onItemClick: (TodoItem) -> Unit,
    onDelete: (TodoItem) -> Unit,
    onUpdate: (TodoItem) -> Unit,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
            )
            .background(LightBackPrimary)
    ) {
        items(todoList, key = { it.id }) { todo ->
            TodoListItem(
                todoItem = todo,
                onCheckboxClick = { onUpdate(todo) },
                onItemClick = { onItemClick(todo) },
                Modifier.animateItemPlacement(
                    tween(durationMillis = 200)
                )
            )
        }
    }


}


@Preview(widthDp = 360, heightDp = 720)
@Composable
fun PreviewToDoItemList() {
    Surface(
        color = LightBackPrimary,
    ) {
        TodoList(
            todoList = getData(),
            onItemClick = {},
            onDelete = {},
            onUpdate = {}
        )
    }

}