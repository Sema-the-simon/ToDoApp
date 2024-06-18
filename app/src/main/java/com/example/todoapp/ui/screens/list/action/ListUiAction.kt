package com.example.todoapp.ui.screens.list.action

import com.example.todoapp.data.model.TodoItem

sealed class ListUiAction {
    data class UpdateTodoItem(val todoItem: TodoItem): ListUiAction()
    data class RemoveTodoItem(val todoItem: TodoItem) : ListUiAction()
}