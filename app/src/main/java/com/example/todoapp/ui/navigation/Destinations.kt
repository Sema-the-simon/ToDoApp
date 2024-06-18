package com.example.todoapp.ui.navigation

interface Destination {
    val route: String
}

object List : Destination {
    override val route: String = "list"
}