package com.example.todoapp.ui.screens.list.action

import com.example.todoapp.data.model.TodoItem

sealed class ListUiAction {
    data class UpdateTodoItem(val todoItemId: String): ListUiAction()
    data class RemoveTodoItem(val todoItemId: String) : ListUiAction()
    data class ChangeFilter(val isFiltered: Boolean) :ListUiAction()
}