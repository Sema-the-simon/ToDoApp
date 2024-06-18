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
    const val ROUT_WITH_ARGS = "edit/{id}"

    override val route: String = "edit"

    val arguments = listOf(
        navArgument(ID) {
            type = NavType.StringType
        }
    )

    fun navToOrderWithArgs(
        id: String = ""
    ): String {
        return "$route/$id"
    }
}