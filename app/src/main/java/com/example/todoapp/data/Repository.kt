package com.example.todoapp.data

import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    val todoItems: StateFlow<List<TodoItem>>
    val isNetworkAvailable: StateFlow<Boolean>
    val isDataSynchronized: StateFlow<Boolean>

    suspend fun countDoneTodos(): Int
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun updateItem(todoItem: TodoItem)
    suspend fun removeItem(id: String)
    suspend fun getTodoItem(id: String): TodoItem?
    suspend fun loadTodoItems()
}