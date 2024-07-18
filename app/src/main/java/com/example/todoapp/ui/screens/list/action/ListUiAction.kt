package com.example.todoapp.ui.screens.list.action

import com.example.todoapp.domain.model.TodoItem

sealed class ListUiAction {
    data class UpdateTodoItem(val todoItemId: String) : ListUiAction()
    data class RemoveTodoItems(val todoItemIds: List<String>) : ListUiAction()
    data class HideTodoItem(val todoItemId: String) : ListUiAction()
    data class ShowHiddenTodoItem(val todoItemId: String) : ListUiAction()
    data class ChangeFilter(val isFiltered: Boolean) : ListUiAction()
    data object RefreshList : ListUiAction()
    data object ClearErrorMessage : ListUiAction()
    data class AddInUndoStack(val todoItem: TodoItem) : ListUiAction()
    data object RemoveLastInUndoStack : ListUiAction()
    data object ClearUndoStack : ListUiAction()
}