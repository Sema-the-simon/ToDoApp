package com.example.todoapp.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.Repository
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.model.UserError
import com.example.todoapp.domain.usecases.FilterListUseCase
import com.example.todoapp.ui.screens.list.action.ListUiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * `ListViewModel` manages the UI state for the main ListScreen.
 *
 * Interacts with the repository and use cases to provide the UI with up-to-date data,
 * handle user actions, and manage state.
 *
 * - **`uiState`**: StateFlow that emits the current UI state.
 * - **`onUiAction`**: Handles different UI actions.
 *
 * `ListUiState`**: Data class representing the state of the UI.
 */

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: Repository,
    private val filterListUseCase: FilterListUseCase
) : ViewModel() {

    private val hiddenElements = hashSetOf<String>()

    private val _uiState: MutableStateFlow<ListUiState> = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = combine(
        _uiState,
        repository.todoItems,
        repository.dataState,
        filterListUseCase.isFiltered
    ) { state, tasks, dataState, isFiltered ->
        state.copy(
            todoItems = filterListUseCase(tasks, isFiltered).filterHidden(),
            countDoneTasks = repository.countDoneTodos(),
            isFiltered = isFiltered,
            isDataSynchronized = dataState.isDataSynchronized,
            isRefreshing = dataState.isDataLoading,
            errorMessage = dataState.errorMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ListUiState()
    )

    private fun List<TodoItem>.filterHidden(): List<TodoItem> =
        this.filter { it.id !in hiddenElements }

    fun onUiAction(action: ListUiAction) {
        when (action) {
            is ListUiAction.UpdateTodoItem -> updateTodoItem(action.todoItemId)
            is ListUiAction.RemoveTodoItems -> removeTodoItems(action.todoItemIds)
            is ListUiAction.HideTodoItem -> hideTodoItem(action.todoItemId)
            is ListUiAction.ShowHiddenTodoItem -> showHiddenTodoItem(action.todoItemId)
            is ListUiAction.ChangeFilter -> changeFilterState(action.isFiltered)
            ListUiAction.RefreshList -> refreshList()
            ListUiAction.ClearErrorMessage -> clearErrorMessage()
        }
    }

    private fun updateTodoItem(todoItemId: String) {
        uiState.value.todoItems.find { it.id == todoItemId }?.let { item ->
            viewModelScope.launch {
                repository.updateItem(
                    item.copy(isDone = !item.isDone)
                )
            }
        }
    }


    private fun removeTodoItems(todoItemIds: List<String>) {
        viewModelScope.launch {
            repository.removeItems(todoItemIds)
        }
    }

    private fun hideTodoItem(todoItemId: String) {
        hiddenElements.add(todoItemId)
        _uiState.update { uiState.value }
    }

    private fun showHiddenTodoItem(todoItemId: String) {
        hiddenElements.remove(todoItemId)
        _uiState.update { uiState.value }
    }

    private fun changeFilterState(isFiltered: Boolean) {
        viewModelScope.launch {
            filterListUseCase.changeFilterState(isFiltered)
        }
    }

    private fun refreshList() {
        viewModelScope.launch {
            repository.loadTodoItems()
        }
    }

    private fun clearErrorMessage() {
        viewModelScope.launch {
            repository.clearErrorMessage()
        }
    }
}

data class ListUiState(
    val todoItems: List<TodoItem> = emptyList(),
    val countDoneTasks: Int = 0,
    val isFiltered: Boolean = false,
    val isDataSynchronized: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: UserError? = null
)

