package com.example.todoapp.data

import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    val todoItems: StateFlow<List<TodoItem>>
    val isTuskFiltered: StateFlow<Boolean>

    fun countDoneTodos(): Int
    fun addTodoItem(todoItem: TodoItem)
    fun updateItem(todoItem: TodoItem)
    fun removeItem(id: String)
    fun getTodoItem(id: String): TodoItem?
    fun changeFilterState(isFiltered: Boolean)
}