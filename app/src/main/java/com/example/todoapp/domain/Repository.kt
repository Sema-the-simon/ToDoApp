package com.example.todoapp.domain

import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.model.UserError
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    val todoItems: StateFlow<List<TodoItem>>
    val dataState: StateFlow<DataState>

    suspend fun countDoneTodos(): Int
    suspend fun loadTodoItems()
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun updateItem(todoItem: TodoItem)
    suspend fun removeItem(id: String)
    suspend fun removeItems(ids: List<String>)
    suspend fun getTodoItem(id: String): TodoItem?
    suspend fun clearErrorMessage()

    data class DataState(
        val isDataLoading: Boolean = false,
        val isDataSynchronized: Boolean = false,
        val errorMessage : UserError? = null
    )
}

