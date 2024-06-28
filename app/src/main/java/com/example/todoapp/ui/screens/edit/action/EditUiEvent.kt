package com.example.todoapp.ui.screens.edit.action

sealed class EditUiEvent {
    data class ShowSnackbar(val message: String): EditUiEvent()
}