package com.example.todoapp.data.db

import androidx.room.ColumnInfo
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.utils.toImportance

data class TodoItemInfoTuple(
    val id: String,
    @ColumnInfo(name = "importance_name") var importance: String,
    var text: String,
    var deadline: Long?,
    var done: Boolean,
    val createdAt: Long,
    var changedAt: Long
) {

    fun toTodoItem(): TodoItem = TodoItem(
        id = id,
        text = text,
        importance = importance.toImportance(),
        deadline = deadline,
        isDone = done,
        creationDate = createdAt,
        modificationDate = changedAt
    )
}