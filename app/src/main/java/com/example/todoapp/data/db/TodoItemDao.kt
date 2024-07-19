package com.example.todoapp.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.todoapp.data.db.entities.TodoItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Upsert(entity = TodoItemEntity::class)
    suspend fun insertNewTodoItemData(todo: TodoItemEntity)

    @Query("SELECT todo.id, importance_name, text, deadline, done, createdAt, changedAt, isDeleted FROM todo " +
            "INNER JOIN importance_levels ON todo.importance_id = importance_levels.id")
    fun observeTodoData(): Flow<List<TodoItemInfoTuple>>

    @Query("SELECT todo.id, importance_name, text, deadline, done, createdAt, changedAt, isDeleted FROM todo " +
            "INNER JOIN importance_levels ON todo.importance_id = importance_levels.id")
    suspend fun getAllTodoData(): List<TodoItemInfoTuple>

    @Query("DELETE FROM todo WHERE id = :todoId")
    suspend fun deleteTodoDataById(todoId: String)

    @Query("DELETE FROM todo WHERE isDeleted = 1")
    suspend fun clearDeleted()

    @Query("SELECT todo.id, importance_name, text, deadline, done, createdAt, changedAt, isDeleted FROM todo\n" +
            "INNER JOIN importance_levels ON todo.importance_id = importance_levels.id\n" +
            "WHERE todo.id = :todoId")
    suspend fun getTodoDataById(todoId: String): TodoItemInfoTuple?

    @Update(entity = TodoItemEntity::class)
    suspend fun updateTodoData(todo: TodoItemEntity)

    @Transaction
    suspend fun replaceAllTodoItems(todoItems: List<TodoItemEntity>) {
        deleteAllTodoItems()
        todoItems.forEach {
            insertNewTodoItemData(it)
        }
    }

    @Query("DELETE FROM todo")
    suspend fun deleteAllTodoItems()
}