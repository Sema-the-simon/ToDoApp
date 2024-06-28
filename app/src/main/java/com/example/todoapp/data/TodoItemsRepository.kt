package com.example.todoapp.data

import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.di.ApplicationScope
import com.example.todoapp.di.DefaultDispatcher
import com.example.todoapp.utils.fakeNetworkOrDatabaseCall
import com.example.todoapp.utils.getData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TodoItemsRepository @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @ApplicationScope private val externalScope: CoroutineScope,
) : Repository {
    private val _todoItems: MutableStateFlow<List<TodoItem>> = MutableStateFlow(getData())
    override val todoItems = _todoItems.asStateFlow()

    private val _isTuskFiltered = MutableStateFlow(false)
    override val isTuskFiltered = _isTuskFiltered.asStateFlow()

    override suspend fun countDoneTodos(): Int = todoItems.value.count { it.isDone }

    override suspend fun addTodoItem(todoItem: TodoItem) = withContext(defaultDispatcher) {
        _todoItems.update {
            val updatedList = todoItems.value.toMutableList()
            updatedList.add(todoItem)
            updatedList.toList()
        }
    }

    override suspend fun updateItem(todoItem: TodoItem) = withContext(defaultDispatcher) {
        val containsTodoItem = _todoItems.value.any { it.id == todoItem.id }
        if (containsTodoItem) {
            _todoItems.update { currentList ->
                currentList.map {
                    when (it.id) {
                        todoItem.id -> todoItem
                        else -> it
                    }
                }
            }
        }
    }

    override suspend fun removeItem(id: String) = withContext(defaultDispatcher) {
        val containsTodoItem = _todoItems.value.any { it.id == id }
        if (containsTodoItem) {
            _todoItems.update { currentList ->
                currentList.filter { it.id != id }
            }
        }
    }

    /**
     * В данной реализации нет ни network ни database вызовов, поэтому для демонстрации механизма
     * отловли ошибок создана ф-ия [fakeNetworkOrDatabaseCall], которая может брость [IOException]
     * обработка исключения перенесена на ui-слой в [EditViewModel.kt].
     *
     */
    override suspend fun getTodoItem(id: String): TodoItem? {
        return _todoItems.value.firstOrNull { it.id == id }
            ?: externalScope.async {
                fakeNetworkOrDatabaseCall(id)
            }.await()
    }

    override suspend fun changeFilterState(isFiltered: Boolean) {
        _isTuskFiltered.update {
            isFiltered
        }
    }
}