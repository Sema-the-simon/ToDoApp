package com.example.todoapp.ui.screens.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Repository
import com.example.todoapp.data.TodoItemsRepository
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.navigation.Edit
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class EditViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository: Repository = TodoItemsRepository()

    private var todoItem = TodoItem(
        id = UUID.randomUUID().toString(),
        text = "",
        importance = Importance.BASIC,
        creationDate = System.currentTimeMillis()
    )
    private var isNewItem: Boolean = true

    private val _uiState = MutableStateFlow(EditUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>(Edit.ID) ?: ""
            repository.getTodoItem(id)?.let { item ->
                todoItem = item
                isNewItem = false

                _uiState.update {
                    uiState.value.copy(
                        text = item.text,
                        importance = item.importance,
                        deadline = item.deadline ?: uiState.value.deadline,
                        isDeadlineSet = item.deadline != null,
                        isNewItem = false
                    )
                }
            }
        }
    }


    fun onUiAction(action: EditUiAction) {
        when (action) {
            EditUiAction.SaveTask -> saveTodoItem()
            EditUiAction.DeleteTask -> removeTodoItem()
            is EditUiAction.UpdateText -> _uiState.update {
                uiState.value.copy(text = action.text)
            }

            is EditUiAction.UpdateDeadlineSet -> _uiState.update {
                uiState.value.copy(isDeadlineSet = action.isDeadlineSet)
            }

            is EditUiAction.UpdateImportance -> _uiState.update {
                uiState.value.copy(importance = action.importance)
            }

            is EditUiAction.UpdateDeadline -> _uiState.update {
                uiState.value.copy(deadline = action.deadline)
            }
        }
    }

    private fun saveTodoItem() {
        if (uiState.value.text.isBlank()) return

        todoItem = todoItem.copy(
            text = uiState.value.text,
            importance = uiState.value.importance,
            deadline = if (uiState.value.isDeadlineSet) uiState.value.deadline else null,
            modificationDate =  if (!isNewItem) System.currentTimeMillis() else null
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (isNewItem)
                repository.addTodoItem(todoItem)
            else
                repository.updateItem(todoItem)

        }
    }

    private fun removeTodoItem() {
        if (!isNewItem)
            viewModelScope.launch(Dispatchers.IO) {
                repository.removeItem(todoItem.id)
            }
    }

}

data class EditUiState(
    val text: String = "",
    val importance: Importance = Importance.BASIC,
    val deadline: Long = System.currentTimeMillis(),
    val isDeadlineSet: Boolean = false,
    val isNewItem: Boolean = true
)