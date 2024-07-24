package com.example.todoapp.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.example.todoapp.R
import com.example.todoapp.data.db.TodoItemInfoTuple
import com.example.todoapp.data.db.entities.TodoItemEntity
import com.example.todoapp.data.network.model.TodoItemDto
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.domain.model.ThemeMode
import com.example.todoapp.domain.model.TodoItem
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Utility functions and constants.
 */


const val IMPORTANCE_IMPORTANT_ID = 3
const val IMPORTANCE_BASIC_ID = 2
const val IMPORTANCE_LOW_ID = 1


fun formatLongToDatePattern(date: Long): String =
    SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(date)

fun String.toImportance(): Importance =
    when (this) {
        "important" -> Importance.IMPORTANT
        "basic" -> Importance.BASIC
        else -> Importance.LOW
    }

fun Importance.toStringResource(): Int {
    return when (this) {
        Importance.LOW -> R.string.importance_low
        Importance.BASIC -> R.string.importance_normal
        Importance.IMPORTANT -> R.string.importance_urgent
    }
}

fun Importance.toServerFormatString(): String {
    return when (this) {
        Importance.LOW -> "low"
        Importance.BASIC -> "basic"
        Importance.IMPORTANT -> "important"
    }
}


fun Importance.getImportanceId(): Int =
    when (this) {
        Importance.IMPORTANT -> IMPORTANCE_IMPORTANT_ID
        Importance.BASIC -> IMPORTANCE_BASIC_ID
        else -> IMPORTANCE_LOW_ID
    }


fun TodoItem.toEntity(isDeleted: Boolean = false): TodoItemEntity =
    TodoItemEntity(
        id = this.id,
        importanceId = this.importance.getImportanceId(),
        text = this.text,
        deadline = this.deadline,
        done = this.isDone,
        createdAt = this.creationDate,
        changedAt = this.modificationDate,
        isDeleted = isDeleted
    )



fun TodoItemInfoTuple.toTodoItem(): TodoItem = TodoItem(
    id = id,
    text = text,
    importance = importance.toImportance(),
    deadline = deadline,
    isDone = done,
    creationDate = createdAt,
    modificationDate = changedAt
)

fun TodoItemDto.toTodoItem(): TodoItem {
    return TodoItem(
        id = this.id,
        text = this.text,
        importance = this.importance.toImportance(),
        isDone = this.done,
        creationDate = this.createdAt,
        deadline = this.deadline,
        modificationDate = this.changedAt
    )
}

fun TodoItem.asDto(userId: String): TodoItemDto {
    return TodoItemDto(
        id = this.id,
        text = this.text,
        importance = this.importance.toServerFormatString(),
        deadline = this.deadline,
        done = this.isDone,
        createdAt = this.creationDate,
        changedAt = this.modificationDate ?: creationDate,
        lastUpdatedBy = userId
    )
}


@Composable
@ReadOnlyComposable
fun isDarkThemeSetUp(themeMode: ThemeMode): Boolean = when (themeMode) {
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
    else -> isSystemInDarkTheme()
}


