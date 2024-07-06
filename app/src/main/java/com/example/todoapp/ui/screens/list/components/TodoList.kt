package com.example.todoapp.ui.screens.list.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme
import com.example.todoapp.utils.getData

@Composable
fun TodoList(
    todoList: List<TodoItem>,
    onItemClick: (String?) -> Unit,
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
            .background(ExtendedTheme.colors.backSecondary),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(todoList, key = { it.id }) { todoItem ->
            SwipedTodoListItem(
                todoItem = todoItem,
                onCheckboxClick = { onUpdate(todoItem) },
                onItemClick = { onItemClick(todoItem.id) },
                onDeleteSwipe = { onDelete(todoItem) },
                onUpdateSwipe = { onUpdate(todoItem) },
                Modifier.animateItem(
                    placementSpec = tween(durationMillis = 150)
                )
            )
        }
        item {
            TextButton(
                onClick = {
                    onItemClick(null)
                },
                modifier = Modifier.animateItem(
                    placementSpec = tween(durationMillis = 200)
                )
            ) {
                Text(
                    text = stringResource(R.string.add_new_task_text_field),
                    color = ExtendedTheme.colors.labelTertiary,
                    style = ExtendedTheme.typography.body,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 42.dp, top = 8.dp, bottom = 8.dp)
                )
            }
        }
    }


}


@Preview(widthDp = 360, heightDp = 720, locale = "ru")
@Composable
fun PreviewToDoItemList(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme(isDarkTheme) {
        TodoList(
            todoList = getData().filter { it.id < 4.toString() },
            onItemClick = {},
            onDelete = {},
            onUpdate = {}
        )

    }
}