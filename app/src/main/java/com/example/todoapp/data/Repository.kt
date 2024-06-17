package com.example.todoapp.data

import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    fun getTodoItems() : StateFlow<List<TodoItem>>
    fun addTodoItem(todoItem: TodoItem)
}