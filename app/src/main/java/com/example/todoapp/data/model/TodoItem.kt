package com.example.todoapp.data.model

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Long? = null,
    val isDone: Boolean,
    val creationDate: Long,
    val modificationDate: Long? = null
)
