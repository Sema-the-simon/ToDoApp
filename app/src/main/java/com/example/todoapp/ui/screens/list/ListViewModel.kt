package com.example.todoapp.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Repository
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.screens.list.action.ListUiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState())
    val uiState = combine(
        _uiState,
        repository.todoItems,
        repository.isTaskFiltered
    ) { state, tasks, isFiltered ->
        state.copy(
            todoItems = tasks.withFilter(isFiltered),
            countDoneTasks = repository.countDoneTodos(),
            isFiltered = isFiltered
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListUiState())

    fun onUiAction(action: ListUiAction) {
        when (action) {
            is ListUiAction.UpdateTodoItem -> updateTodoItem(action.todoItemId)
            is ListUiAction.RemoveTodoItem -> removeTodoItem(action.todoItemId)
            is ListUiAction.ChangeFilter -> changeFilterState(action.isFiltered)
        }
    }

    private fun List<TodoItem>.withFilter(isFiltered: Boolean): List<TodoItem> =
        if (isFiltered) this.filter { !it.isDone } else this

    private fun updateTodoItem(todoItemId: String) {
        uiState.value.todoItems.find { it.id == todoItemId }?.let { item ->
            viewModelScope.launch {
                repository.updateItem(
                    item.copy(isDone = item.let { !it.isDone })
                )
            }
        }
    }

    private fun removeTodoItem(todoItemId: String) {
        viewModelScope.launch {
            repository.removeItem(todoItemId)
        }
    }

    private fun changeFilterState(isFiltered: Boolean) {
        viewModelScope.launch {
            repository.changeFilterState(isFiltered)
        }
    }
}

data class ListUiState(
    val todoItems: List<TodoItem> = emptyList(),
    val countDoneTasks: Int = 0,
    val isFiltered: Boolean = false
)