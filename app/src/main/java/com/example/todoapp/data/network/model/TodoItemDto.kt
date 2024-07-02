package com.example.todoapp.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemDto(
    @SerialName("id")
    val id: String,
    @SerialName("text")
    val text: String,
    @SerialName("importance")
    val importance: String,         // importance = low | basic | important
    @SerialName("deadline")
    val deadline: Long? = null,     // int64, может отсутствовать, тогда нет
    @SerialName("done")
    val done: Boolean,
    @SerialName("color")
    val color: String? = null,      // "#FFFFFF" может отсутствовать
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("changed_at")
    val changedAtt: Long,
    @SerialName("last_updated_by")
    val lastUpdatedBy: String       //<device id>
)