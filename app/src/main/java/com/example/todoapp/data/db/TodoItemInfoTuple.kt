package com.example.todoapp.data.db

import androidx.room.ColumnInfo

data class TodoItemInfoTuple(
    val id: String,
    @ColumnInfo(name = "importance_name") var importance: String,
    val text: String,
    val deadline: Long?,
    val done: Boolean,
    val createdAt: Long,
    val changedAt: Long,
    val isDeleted: Boolean
)