package com.example.todoapp.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents the response body for network calls, containing
 *  either list of [TodoItemDto] or single [TodoItemDto]
 *  also containing status and revision */

sealed interface ResponseBody {
    @Serializable
    data class TodoList(
        @SerialName("status")
        val status: String,
        @SerialName("list")
        val list: List<TodoItemDto>,
        @SerialName("revision")
        val revision: Int
    ) : ResponseBody

    @Serializable
    data class TodoElement(
        @SerialName("status")
        val status: String,
        @SerialName("element")
        val element: TodoItemDto,
        @SerialName("revision")
        val revision: Int
    ) : ResponseBody
}

