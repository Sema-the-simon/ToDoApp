package com.example.todoapp.data

import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.utils.UserError
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    val todoItems: StateFlow<List<TodoItem>>
    val dataState: StateFlow<DataState>

    suspend fun countDoneTodos(): Int
    suspend fun loadTodoItems()
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun updateItem(todoItem: TodoItem)
    suspend fun removeItem(id: String)
    suspend fun getTodoItem(id: String): TodoItem?
    suspend fun clearErrorMessage()

    data class DataState(
        val isDataLoading: Boolean = false,
        val isDataSynchronized: Boolean = false,
        val errorMessage : UserError? = null
    )
}

