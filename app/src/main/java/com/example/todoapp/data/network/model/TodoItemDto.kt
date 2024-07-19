package com.example.todoapp.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Data Transfer Object for a ToDoItem, used for network communication. */

@Serializable
data class TodoItemDto(
    @SerialName("id")
    val id: String,
    @SerialName("text")
    val text: String,
    @SerialName("importance")
    val importance: String,
    @SerialName("deadline")
    val deadline: Long? = null,
    @SerialName("done")
    val done: Boolean,
    @SerialName("color")
    val color: String? = null,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("changed_at")
    val changedAt: Long,
    @SerialName("last_updated_by")
    val lastUpdatedBy: String,
    @SerialName("files")
    val files: List<String>? = null,
)