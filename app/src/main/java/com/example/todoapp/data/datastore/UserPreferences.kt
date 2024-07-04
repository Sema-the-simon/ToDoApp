package com.example.todoapp.data.datastore

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserPreferences(
    val userId: String = UUID.randomUUID().toString(),
    val isListFilter: Boolean = false,
)