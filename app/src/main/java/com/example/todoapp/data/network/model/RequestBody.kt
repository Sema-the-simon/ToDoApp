package com.example.todoapp.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents the request body for network calls, containing either
 *  list of [TodoItemDto] or single [TodoItemDto]. */

sealed interface RequestBody {
    @Serializable
    data class TodoList(
        @SerialName("list")
        val list: List<TodoItemDto>
    ) : RequestBody

    @Serializable
    data class TodoElement(
        @SerialName("element")
        val element: TodoItemDto,
    ) : RequestBody
}