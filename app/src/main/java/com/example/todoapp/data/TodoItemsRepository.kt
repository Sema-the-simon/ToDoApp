package com.example.todoapp.data

import android.util.Log
import com.example.todoapp.data.datastore.DataStoreManager
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.network.Api
import com.example.todoapp.data.network.model.RequestBody
import com.example.todoapp.data.network.model.ResponseBody
import com.example.todoapp.data.network.model.Result
import com.example.todoapp.data.network.model.asDto
import com.example.todoapp.data.network.model.handle
import com.example.todoapp.data.network.model.toTodoItem
import com.example.todoapp.di.ApplicationScope
import com.example.todoapp.di.DefaultDispatcher
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TodoItemsRepository @Inject constructor(
    private val api: Api,
    private val dataSource: TodoItemsDataSource,
    private val dataStoreManager: DataStoreManager,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    @ApplicationScope
    private val externalScope: CoroutineScope,
) : Repository {
    private val _todoItems: MutableStateFlow<List<TodoItem>> = MutableStateFlow(emptyList())
    override val todoItems = _todoItems.asStateFlow()

    override val isNetworkAvailable = dataSource.internetConnectionState

    private val _isDataSynchronized = MutableStateFlow(false)
    override val isDataSynchronized = combine(
        _isDataSynchronized,
        isNetworkAvailable,
    ) { dataSync, internetConnection ->
        val newValue = dataSync && internetConnection
        val oldValue = _isDataSynchronized.value
        //if (newValue != oldValue)
        _isDataSynchronized.update {
            newValue
        }
        newValue
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    private var revision = 0
    private var userId = ""

    private fun getUserId() = externalScope.launch {
        dataStoreManager.userPreferences.collectLatest { pref ->
            userId = pref.userId
        }
    }

    init {
        getUserId()
        dataSource.loadTodoItemsOnStart()
        dataSource.loadTodoItemsPeriodically()
    }

    private fun loggError(error: Result.Error) {
        val code = if (error.errorCode != -1) "[${error.errorCode}]: " else ""
        val message = "$code${error.errorMessage}"
        Log.d("networkTEST", message)
    }

    private fun dataToString(data: ResponseBody): String {
        return when (data) {
            is ResponseBody.TodoElement -> {
                data.element.toString()
            }

            is ResponseBody.TodoList -> {
                "\n" + data.list.joinToString(separator = "\n")
            }
        }
    }

    private fun loggSuccess(data: ResponseBody) {
        val message = when (data) {
            is ResponseBody.TodoElement -> {
                """[${data.status.uppercase()}]: 
                    |   el - ${data.element}
                    |   revision - ${data.revision}""".trimMargin()
            }

            is ResponseBody.TodoList -> {
                """[${data.status.uppercase()}]: 
                    |   list - ${dataToString(data)}
                    |   revision - ${data.revision}""".trimMargin()
            }
        }
        Log.d("networkTEST", message)
    }

    private fun MutableList<TodoItem>.mergeWith(list: List<TodoItem>) {
        val setIds = this.map { it.id }.toHashSet()
        list.forEach { el ->
            if (el.id in setIds)
                this.map { if (it.id == el.id) el else it }
            else
                this.add(el)
        }
    }

    override suspend fun loadTodoItems() = withContext(defaultDispatcher) {
        val result = externalScope.async { api.getTodoList() }.await()
        result.handle(
            onSuccess = { data ->
                val newElements = data.list.map { it.toTodoItem() }
                _todoItems.update {
                    todoItems.value.toMutableList().apply { mergeWith(newElements) }
                }
                revision = data.revision
                _isDataSynchronized.update { true }
                loggSuccess(data)
            },
            onError = { error ->
                loggError(error)
                val errorMessage = if (error.errorCode == HttpStatusCode.InternalServerError.value)
                    "Server error: " + error.errorMessage
                else
                    "Something went wrong"
                throw IOException(errorMessage)
            },
        )
    }

    override suspend fun countDoneTodos(): Int = todoItems.value.count { it.isDone }

    private fun createElementRequest(element: TodoItem): RequestBody.TodoElement =
        RequestBody.TodoElement(element.asDto(userId))

    override suspend fun addTodoItem(todoItem: TodoItem) = withContext(defaultDispatcher) {
        _todoItems.update {
            val updatedList = todoItems.value.toMutableList()
            updatedList.add(todoItem)
            updatedList.toList()
        }
        if (isNetworkAvailable.value) {
            val result = externalScope.async {
                api.addItem(revision, createElementRequest(todoItem))
            }.await()

            result.handle(
                onSuccess = { data ->
                    revision = data.revision
                    loggSuccess(data)
                },
                onError = { error ->
                    loggError(error)
                },
            )
        }
    }

    override suspend fun updateItem(todoItem: TodoItem) = withContext(defaultDispatcher) {
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
            if (isNetworkAvailable.value) {
                val result = externalScope.async {
                    api.editItem(revision, createElementRequest(todoItem))
                }.await()

                result.handle(
                    onSuccess = { data ->
                        revision = data.revision
                        loggSuccess(data)
                    },
                    onError = { error ->
                        loggError(error)
                    },
                )
            }

        }
    }

    override suspend fun removeItem(id: String) = withContext(defaultDispatcher) {
        val containsTodoItem = _todoItems.value.any { it.id == id }
        if (containsTodoItem) {
            _todoItems.update { currentList ->
                currentList.filter { it.id != id }
            }
            if (isNetworkAvailable.value) {
                val result = externalScope.async { api.deleteItem(revision, id) }.await()

                result.handle(
                    onSuccess = { data ->
                        revision = data.revision
                        loggSuccess(data)
                    },
                    onError = { error ->
                        loggError(error)
                    },
                )
            }
        }
    }

    override suspend fun getTodoItem(id: String): TodoItem? = withContext(defaultDispatcher) {
        _todoItems.value.firstOrNull { it.id == id }
    }
}