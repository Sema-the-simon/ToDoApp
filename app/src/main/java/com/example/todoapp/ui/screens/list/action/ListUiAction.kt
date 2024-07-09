package com.example.todoapp.ui.screens.list.action

sealed class ListUiAction {
    data class UpdateTodoItem(val todoItemId: String): ListUiAction()
    data class RemoveTodoItem(val todoItemId: String) : ListUiAction()
    data class ChangeFilter(val isFiltered: Boolean) :ListUiAction()
    data object RefreshList: ListUiAction()
    data object ClearErrorMessage: ListUiAction()
}