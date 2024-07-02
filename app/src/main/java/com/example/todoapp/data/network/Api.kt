package com.example.todoapp.data.network

import com.example.todoapp.data.network.model.RequestBody
import com.example.todoapp.data.network.model.Result
import com.example.todoapp.data.network.model.ResponseBody.*


interface Api {
    suspend fun getTodoList(): Result<TodoList>
    suspend fun updateList(revision: Int, req: RequestBody.TodoList): Result<TodoList>
    suspend fun getItemById(revision: Int, id: String): Result<TodoElement>
    suspend fun addItem(revision: Int, req: RequestBody.TodoElement): Result<TodoElement>
    suspend fun editItem(revision: Int, req: RequestBody.TodoElement): Result<TodoElement>
    suspend fun deleteItem(revision: Int, id: String): Result<TodoElement>
}
