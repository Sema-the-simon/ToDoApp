package com.example.todoapp.ui.screens.edit.action

/** Represent UI Events that may occurs on [EditScreen]. */

sealed class EditUiEvent {
    data class ShowSnackbar(val message: String): EditUiEvent()
}