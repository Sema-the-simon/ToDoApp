package com.example.todoapp.data.network.model

import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.utils.toImportance
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