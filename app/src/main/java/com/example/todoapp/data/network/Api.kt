package com.example.todoapp.data.network

import com.example.todoapp.data.network.model.RequestBody
import com.example.todoapp.data.network.model.ResponseResult
import com.example.todoapp.data.network.model.ResponseBody.*


interface Api {
    suspend fun getTodoList(): ResponseResult<TodoList>
    suspend fun updateList(revision: Int, req: RequestBody.TodoList): ResponseResult<TodoList>
    suspend fun getItemById(revision: Int, id: String): ResponseResult<TodoElement>
    suspend fun addItem(revision: Int, req: RequestBody.TodoElement): ResponseResult<TodoElement>
    suspend fun editItem(revision: Int, req: RequestBody.TodoElement): ResponseResult<TodoElement>
    suspend fun deleteItem(revision: Int, id: String): ResponseResult<TodoElement>
}
