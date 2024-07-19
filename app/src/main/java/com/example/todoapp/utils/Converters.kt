package com.example.todoapp.utils

import com.example.todoapp.data.db.entities.TodoItemEntity
import com.example.todoapp.domain.model.Importance
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

fun Importance.getImportanceId(): Int =
    when (this) {
        Importance.IMPORTANT -> IMPORTANCE_IMPORTANT_ID
        Importance.BASIC -> IMPORTANCE_BASIC_ID
        else -> IMPORTANCE_LOW_ID
    }

fun importanceById(id: Int): Importance =
    when (id) {
        IMPORTANCE_IMPORTANT_ID -> Importance.IMPORTANT
        IMPORTANCE_BASIC_ID -> Importance.BASIC
        else -> Importance.LOW
    }

fun TodoItemEntity.toTodoItem(): TodoItem =
    TodoItem(
        id = this.id,
        text = this.text,
        importance = importanceById(this.importanceId),
        deadline = this.deadline,
        isDone = this.done,
        creationDate = this.createdAt,
        modificationDate = this.changedAt
    )

