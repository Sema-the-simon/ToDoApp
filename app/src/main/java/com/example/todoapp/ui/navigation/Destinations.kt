package com.example.todoapp.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    data object List : Destination()

    @Serializable
    data class Edit(val id: String?) : Destination()
}