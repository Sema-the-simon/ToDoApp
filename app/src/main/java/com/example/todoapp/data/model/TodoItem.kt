package com.example.todoapp.data.model

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    var deadline: Long? = null,
    var isDone: Boolean = false,
    val creationDate: Long,
    var modificationDate: Long? = null
) : RecyclerItem
