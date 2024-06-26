package com.example.todoapp.data

import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.utils.getData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


class TodoItemsRepository @Inject constructor() : Repository {
    private val _todoItems: MutableStateFlow<List<TodoItem>> = MutableStateFlow(getData())
    override val todoItems = _todoItems.asStateFlow()
    private val _isTuskFiltered = MutableStateFlow(false)
    override val isTuskFiltered = _isTuskFiltered.asStateFlow()

    override fun countDoneTodos(): Int = _todoItems.value.count { it.isDone }

    override fun addTodoItem(todoItem: TodoItem) {
        _todoItems.update {
            val updatedList = todoItems.value.toMutableList()
            updatedList.add(todoItem)
            updatedList.toList()
        }
    }

    override fun updateItem(todoItem: TodoItem) {
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

    override fun removeItem(id: String) {
        val containsTodoItem = _todoItems.value.any { it.id == id }
        if (containsTodoItem) {
            _todoItems.update { currentList ->
                currentList.filter { it.id != id }
            }
        }
    }

    override fun getTodoItem(id: String): TodoItem? = _todoItems.value.firstOrNull { it.id == id }
    override fun changeFilterState(isFiltered: Boolean) {
        _isTuskFiltered.update {
            isFiltered
        }
    }
}