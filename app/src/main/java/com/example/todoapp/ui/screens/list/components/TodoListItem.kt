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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.themes.LightBackPrimary
import com.example.todoapp.ui.themes.LightBackSecondary
import com.example.todoapp.ui.themes.LightLabelPrimary
import com.example.todoapp.ui.themes.LightLabelTertiary
import com.example.todoapp.ui.themes.LightSupportSeparator
import com.example.todoapp.utils.formatLongToDatePattern

@Composable
fun TodoListItem(
    todoItem: TodoItem,
    onCheckboxClick: () -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(LightBackSecondary),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = todoItem.isDone,
            onCheckedChange = { onCheckboxClick() },
            colors = CheckboxDefaults.colors(
                uncheckedColor = if (todoItem.importance == Importance.IMPORTANT) Color.Red
                else LightSupportSeparator,
                checkedColor = Color.Green
            ),
            modifier = if (todoItem.importance == Importance.IMPORTANT)
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
            modifier = Modifier
                .clickable { onItemClick() }
                .padding(vertical = 12.dp)

        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = todoItem.text,
                    color = if (todoItem.isDone) LightLabelTertiary
                    else LightLabelPrimary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        textDecoration = if (todoItem.isDone) TextDecoration.LineThrough
                        else TextDecoration.None
                    )
                )
                if (todoItem.deadline != null)
                    Text(
                        text = formatLongToDatePattern(todoItem.deadline!!),
                        color = LightLabelTertiary,
                        fontSize = 14.sp
                    )
            }

            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Todo info",
                tint = LightSupportSeparator,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 720)
@Composable
private fun TodoItemPreview() {
    val date = Calendar.getInstance()
    Column {
        TodoListItem(
            todoItem = TodoItem(
                id = "0",
                isDone = false,
                text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, " +
                        "но точно нужно чтобы показать как обрезается " +
                        "эта часть текста не видна",
                importance = Importance.BASIC,
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
                importance = Importance.IMPORTANT,
                creationDate = Calendar.getInstance().timeInMillis,
                deadline = date.timeInMillis

            ),
            onCheckboxClick = {},
            onItemClick = {},
        )
        TodoListItem(
            todoItem = TodoItem(
                id = "2",
                isDone = true,
                text = "Купить что-то",
                importance = Importance.IMPORTANT,
                creationDate = Calendar.getInstance().timeInMillis,
            ),
            onCheckboxClick = {},
            onItemClick = {},
        )
    }


}