package com.example.todoapp.data.datastore

import kotlinx.serialization.Serializable
import java.util.UUID

/** Represents user preferences with a unique [userId] and list filter state - [isListFilter]. */
@Serializable
data class UserPreferences(
    val userId: String = UUID.randomUUID().toString(),
    val isListFilter: Boolean = false,
)