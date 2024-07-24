package com.example.todoapp.ui.screens.list.components

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.R
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.domain.model.Importance.BASIC
import com.example.todoapp.domain.model.Importance.IMPORTANT
import com.example.todoapp.domain.model.Importance.LOW
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme
import com.example.todoapp.utils.formatLongToDatePattern
import com.example.todoapp.utils.toStringResource

@Composable
fun TodoListItem(
    todoItem: TodoItem,
    onCheckboxClick: () -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val taskDoneState = if (todoItem.isDone) stringResource(R.string.done) else stringResource(R.string.not_done)
    val taskImportanceState = stringResource(todoItem.importance.toStringResource())
    val taskStateDescription =
        stringResource(R.string.task_state_description, taskDoneState, taskImportanceState)
    val taskContent = stringResource(R.string.task_text_description, todoItem.text)
    Row(
        modifier = modifier
            .background(ExtendedTheme.colors.backSecondary)
            .semantics(mergeDescendants = true) {

            },
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = todoItem.isDone,
            onCheckedChange = { onCheckboxClick() },
            colors = CheckboxDefaults.colors(
                uncheckedColor = if (todoItem.importance == IMPORTANT) Color.Red
                else ExtendedTheme.colors.supportSeparator,
                checkedColor = Color.Green
            ),
            modifier = if (todoItem.importance == IMPORTANT)
                Modifier
                    .padding(start = 4.dp)
                    .drawBehind {
                        scale(0.4f) {
                            drawRect(
                                color = Color.Red,
                                alpha = 0.15f,
                            )
                        }

                    } else Modifier.padding(start = 4.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClickLabel = stringResource(R.string.on_item_click_description)) { onItemClick() }
                .padding(vertical = 12.dp)
                .semantics(mergeDescendants = true) {
                    stateDescription = taskStateDescription
                    contentDescription = taskContent
                }

        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!todoItem.isDone) {
                        ImportanceSymbol(todoItem.importance)
                    }
                    Text(
                        text = todoItem.text,
                        color = if (todoItem.isDone) ExtendedTheme.colors.labelTertiary
                        else ExtendedTheme.colors.labelPrimary,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = ExtendedTheme.typography.body.copy(
                            textDecoration = if (todoItem.isDone) TextDecoration.LineThrough
                            else TextDecoration.None
                        ),
                        modifier = Modifier.clearAndSetSemantics {  }
                    )
                }
                if (todoItem.deadline != null)
                    Text(
                        text = formatLongToDatePattern(todoItem.deadline),
                        color = ExtendedTheme.colors.labelTertiary,
                        style = ExtendedTheme.typography.subhead
                    )
            }

            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = ExtendedTheme.colors.supportSeparator,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}

@Composable
fun ImportanceSymbol(importance: Importance) {
    when (importance) {
        LOW -> {
            Icon(
                painter = painterResource(id = R.drawable.low_priority_arrow),
                contentDescription = null,
                tint = ExtendedTheme.colors.labelSecondary,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }

        IMPORTANT -> {
            Text(
                text = "!!",
                fontSize = 24.sp,
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }

        else -> {}
    }
}

@Preview(widthDp = 360, heightDp = 720)
@Composable
private fun TodoItemPreview(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    val date = Calendar.getInstance()
    TodoAppTheme(isDarkTheme) {
        Column {
            TodoListItem(
                todoItem = TodoItem(
                    id = "0",
                    isDone = false,
                    text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, " +
                            "но точно нужно чтобы показать как обрезается " +
                            "эта часть текста не видна",
                    importance = BASIC,
                    creationDate = date.timeInMillis
                ),
                onCheckboxClick = {},
                onItemClick = {},
            )
            date.add(Calendar.DAY_OF_WEEK, 2)
            TodoListItem(
                todoItem = TodoItem(
                    id = "1",
                    isDone = false,
                    text = "Купить что-то",
                    importance = IMPORTANT,
                    creationDate = Calendar.getInstance().timeInMillis,
                    deadline = date.timeInMillis

                ),
                onCheckboxClick = {},
                onItemClick = {},
            )
            TodoListItem(
                todoItem = TodoItem(
                    id = "2",
                    isDone = false,
                    text = "Купить что-то",
                    importance = LOW,
                    creationDate = Calendar.getInstance().timeInMillis,
                ),
                onCheckboxClick = {},
                onItemClick = {},
            )
            TodoListItem(
                todoItem = TodoItem(
                    id = "2",
                    isDone = true,
                    text = "Купить что-то",
                    importance = IMPORTANT,
                    creationDate = Calendar.getInstance().timeInMillis,
                ),
                onCheckboxClick = {},
                onItemClick = {},
            )
        }
    }
}