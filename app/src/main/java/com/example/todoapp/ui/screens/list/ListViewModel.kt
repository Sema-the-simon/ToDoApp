package com.example.todoapp.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Repository
import com.example.todoapp.data.TodoItemsRepository
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.screens.list.action.ListUiAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val repository: Repository = TodoItemsRepository()
    private val _uiState = MutableStateFlow(ListUiState())
    val uiState = combine(
        _uiState,
        repository.todoItems,
    ) { state, tasks ->
        state.copy(
            todoItems = tasks,
            countDoneTasks = repository.countDoneTodos()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListUiState())

    fun onUiAction(action: ListUiAction) {
        when (action) {
            is ListUiAction.UpdateTodoItem -> updateTodoItem(action.todoItem)
            is ListUiAction.RemoveTodoItem -> removeTodoItem(action.todoItem)
        }
    }

    private fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.updateItem(todoItem)
        }
    }

    private fun removeTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.removeItem(todoItem.id)
        }
    }
}

data class ListUiState(
    val todoItems: List<TodoItem> = emptyList(),
    val countDoneTasks: Int = 0
)