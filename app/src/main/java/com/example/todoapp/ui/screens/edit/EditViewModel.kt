package com.example.todoapp.ui.screens.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.todoapp.data.Repository
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.navigation.Destination
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.screens.edit.action.EditUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var todoItem = TodoItem(
        id = UUID.randomUUID().toString(),
        text = "",
        importance = Importance.BASIC,
        isDone = false,
        creationDate = System.currentTimeMillis()
    )
    private var isNewItem: Boolean = true

    private val _uiState = MutableStateFlow(EditUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<EditUiEvent>()
    val uiEvent: Flow<EditUiEvent> = _uiEvent.receiveAsFlow()

    init {
        getTodoItemFromRepository()
    }

    private fun getTodoItemFromRepository() {
        viewModelScope.launch {
            savedStateHandle.toRoute<Destination.Edit>().id?.let { id ->
                try {
                    todoItem = repository.getTodoItem(id) ?: return@let
                    isNewItem = false
                } catch (ioe: IOException) {
                    _uiEvent.send(EditUiEvent.ShowSnackbar(ioe.message ?: "Something went wrong"))
                }
                _uiState.update {
                    uiState.value.copy(
                        text = todoItem.text,
                        importance = todoItem.importance,
                        deadline = todoItem.deadline ?: uiState.value.deadline,
                        isDeadlineSet = todoItem.deadline != null,
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

            is EditUiAction.UpdateDialogVisibility -> _uiState.update {
                uiState.value.copy(isDialogVisible = action.isDialogVisible)
            }
        }
    }

    private fun saveTodoItem() {
        if (uiState.value.text.isBlank()) return

        todoItem = todoItem.copy(
            text = uiState.value.text,
            importance = uiState.value.importance,
            deadline = if (uiState.value.isDeadlineSet) uiState.value.deadline else null,
            modificationDate = if (!isNewItem) System.currentTimeMillis() else null
        )

        viewModelScope.launch {
            if (isNewItem)
                repository.addTodoItem(todoItem)
            else
                repository.updateItem(todoItem)

        }
    }

    private fun removeTodoItem() {
        if (!isNewItem)
            viewModelScope.launch {
                repository.removeItem(todoItem.id)
            }
    }

}

data class EditUiState(
    val text: String = "",
    val importance: Importance = Importance.BASIC,
    val deadline: Long = System.currentTimeMillis(),
    val isDeadlineSet: Boolean = false,
    val isNewItem: Boolean = true,
    val isDialogVisible: Boolean = false
)