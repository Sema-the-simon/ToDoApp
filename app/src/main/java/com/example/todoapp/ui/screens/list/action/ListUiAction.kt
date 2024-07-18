package com.example.todoapp.ui.screens.list.action

sealed class ListUiAction {
    data class UpdateTodoItem(val todoItemId: String) : ListUiAction()
    data class RemoveTodoItems(val todoItemIds: List<String>) : ListUiAction()
    data class HideTodoItem(val todoItemId: String) : ListUiAction()
    data class ShowHiddenTodoItem(val todoItemId: String) : ListUiAction()
    data class ChangeFilter(val isFiltered: Boolean) : ListUiAction()
    data object RefreshList : ListUiAction()
    data object ClearErrorMessage : ListUiAction()
}