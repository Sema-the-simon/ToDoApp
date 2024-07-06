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
fun TodoItem.toEntity() =
    TodoItemEntity(
        id = this.id,
        importanceId = this.importance.getImportanceId(),
        text = this.text,
        deadline = this.deadline,
        done = this.isDone,
        createdAt = this.creationDate,
        changedAt = this.modificationDate
    )