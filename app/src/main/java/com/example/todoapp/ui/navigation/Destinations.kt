package com.example.todoapp.ui.navigation

import kotlinx.serialization.Serializable

/** Represent different nav destinations in the app. */

sealed class Destination {
    @Serializable
    data class List(val hideElementId: String? = null) : Destination()

    @Serializable
    data class Edit(val id: String?) : Destination()

    @Serializable
    data object Settings : Destination()
}