package com.example.todoapp.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Repository
import com.example.todoapp.data.datastore.DataStoreManager
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.screens.list.action.ListUiAction
import com.example.todoapp.utils.UserError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _uiState: MutableStateFlow<ListUiState> = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = combine(
        _uiState,
        repository.todoItems,
        repository.dataState,
        dataStoreManager.userPreferences
    ) { state, tasks, dataState, pref ->
        state.copy(
            todoItems = tasks.withFilter(pref.isListFilter),
            countDoneTasks = repository.countDoneTodos(),
            isFiltered = pref.isListFilter,
            isDataSynchronized = dataState.isDataSynchronized,
            isRefreshing = dataState.isDataLoading,
            errorMessage = dataState.errorMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ListUiState()
    )

    fun onUiAction(action: ListUiAction) {
        when (action) {
            is ListUiAction.UpdateTodoItem -> updateTodoItem(action.todoItemId)
            is ListUiAction.RemoveTodoItem -> removeTodoItem(action.todoItemId)
            is ListUiAction.ChangeFilter -> changeFilterState(action.isFiltered)
            ListUiAction.RefreshList -> refreshList()
            ListUiAction.ClearErrorMessage -> clearErrorMessage()
        }
    }

    private fun List<TodoItem>.withFilter(isFiltered: Boolean): List<TodoItem> =
        if (isFiltered) this.filter { !it.isDone } else this

    private fun updateTodoItem(todoItemId: String) {
        uiState.value.todoItems.find { it.id == todoItemId }?.let { item ->
            viewModelScope.launch {
                repository.updateItem(
                    item.copy(isDone = !item.isDone)
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
            dataStoreManager.saveFilterState(isFiltered)
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
    val isDataSynchronized: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: UserError? = null
)

