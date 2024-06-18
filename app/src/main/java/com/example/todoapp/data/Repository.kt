package com.example.todoapp.data

import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    val todoItems: StateFlow<List<TodoItem>>
    fun addTodoItem(todoItem: TodoItem)
    fun countDoneTodos(): Int
    fun updateItem(todoItem: TodoItem)
    fun removeItem(id: String)
}