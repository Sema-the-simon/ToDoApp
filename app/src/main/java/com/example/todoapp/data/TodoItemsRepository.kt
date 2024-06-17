package com.example.todoapp.data

import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TodoItemsRepository() : Repository {
    private val todoItems: MutableList<TodoItem> = mutableListOf()
    private val todoItemsFlow: MutableStateFlow<List<TodoItem>> = MutableStateFlow(mutableListOf())

    override fun getTodoItems(): StateFlow<List<TodoItem>> = todoItemsFlow.asStateFlow()

    override fun addTodoItem(todoItem: TodoItem) {
        todoItems.add(todoItem)
        todoItemsFlow.update {
            todoItems
        }
    }
}