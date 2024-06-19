package com.example.todoapp.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route: String
}

object List : Destination {
    override val route: String = "list"
}

object Edit : Destination {
    const val ID = "id"
    private const val ROUTE = "edit"

    override val route: String =  "$ROUTE/{$ID}"

    val arguments = listOf(
        navArgument(ID) {
            type = NavType.StringType
        }
    )

    fun navToEditWithArgs(
        id: String = ""
    ): String {
        return "$ROUTE/$id"
    }
}