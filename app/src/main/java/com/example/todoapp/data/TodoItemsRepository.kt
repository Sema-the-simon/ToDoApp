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
import com.example.todoapp.utils.UserError
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
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

    private val _dataState: MutableStateFlow<Repository.DataState> =
        MutableStateFlow(Repository.DataState())
    override val dataState: StateFlow<Repository.DataState> = _dataState.asStateFlow()
    private var isDataLoading: Boolean
        get() = dataState.value.isDataLoading
        set(value) {
            _dataState.update { it.copy(isDataLoading = value) }
        }

    private var isDataSynchronized: Boolean
        get() = dataState.value.isDataSynchronized
        set(value) {
            _dataState.update { it.copy(isDataSynchronized = value) }
        }

    private var errorMessage: UserError?
        get() = dataState.value.errorMessage
        set(value) {
            _dataState.update { it.copy(errorMessage = value) }
        }
    //        combine(
//        _isDataSynchronized,
//        isNetworkAvailable,
//    ) { dataSync, internetConnection ->
//        val newValue = dataSync && internetConnection
//        _isDataSynchronized.update {
//            newValue
//        }
//        newValue
//    }.stateIn(
//        scope = externalScope,
//        started = SharingStarted.WhileSubscribed(5_000),
//        initialValue = false
//    )

    private val isNetworkAvailable = dataSource.internetConnectionState
    private val revisionMutex = Mutex()
    private var revision = 0
    private var userId = ""

    private fun getUserId() = externalScope.launch {
        dataStoreManager.userPreferences.collectLatest { pref ->
            userId = pref.userId
        }
    }

    init {
        getUserId()
        dataSource.loadTodoItemsOnce()
        dataSource.loadTodoItemsPeriodically()
    }

    override suspend fun countDoneTodos(): Int = todoItems.value.count { it.isDone }

    private fun loggError(error: Result.Error) {
        val code = if (error.errorCode != -1) "[${error.errorCode}]: " else ""
        val message = "$code${error.errorMessage}"
        Log.d("networkTEST", message)
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

    private fun MutableList<TodoItem>.mergeWith(list: List<TodoItem>) {
        val setIds = this.map { it.id }.toHashSet()
        list.forEach { el ->
            if (el.id in setIds)
                this.map { if (it.id == el.id) el else it }
            else
                this.add(el)
        }
    }

    private suspend fun synchronizedNetworkRun(action: suspend CoroutineScope.() -> Unit) {
        if (isNetworkAvailable.value) {
            externalScope.launch {
                revisionMutex.withLock {
                    action()
                }
            }
        } else {
            if (isDataLoading) {
                delay(10)
                isDataLoading = false
                errorMessage = UserError.NoInternetConnection
            }
            isDataSynchronized = false
        }
    }

    private val handleServerError: (error: Result.Error) -> Unit = { error ->
        loggError(error)
        errorMessage = when (error.errorCode) {
            HttpStatusCode.InternalServerError.value -> UserError.InternalServerError
            HttpStatusCode.BadRequest.value -> UserError.UnsynchronizedData
            HttpStatusCode.Unauthorized.value -> UserError.Unauthorized
            HttpStatusCode.NotFound.value -> UserError.NotFound
            else -> UserError.Unexpected
        }
    }

    override suspend fun loadTodoItems() = withContext(defaultDispatcher) {
        isDataLoading = true
        synchronizedNetworkRun {
            val result = async { api.getTodoList() }.await()
            result.handle(
                onSuccess = { data ->
                    val newElements = data.list.map { it.toTodoItem() }
                    _todoItems.update {
                        todoItems.value.toMutableList().apply { mergeWith(newElements) }
                    }
                    revision = data.revision
                    isDataLoading = false
                    isDataSynchronized = true

                    syncWithServer()

                    loggSuccess(data)
                },
                onError = handleServerError,
            )
        }
    }

    private fun createElementRequest(element: TodoItem): RequestBody.TodoElement =
        RequestBody.TodoElement(element.asDto(userId))

    private fun createListRequest(list: List<TodoItem>): RequestBody.TodoList =
        RequestBody.TodoList(list.map { it.asDto(userId) })

    private suspend fun syncWithServer() {
        synchronizedNetworkRun {
            val result = async {
                api.updateList(revision, createListRequest(todoItems.value))
            }.await()
            result.handle(
                onSuccess = { data ->
                    revision = data.revision
                    loggSuccess(data)
                },
                onError = handleServerError,
            )
        }
    }

    override suspend fun addTodoItem(todoItem: TodoItem) = withContext(defaultDispatcher) {
        _todoItems.update {
            val updatedList = todoItems.value.toMutableList()
            updatedList.add(todoItem)
            updatedList.toList()
        }
        synchronizedNetworkRun {
            val result = async {
                api.addItem(revision, createElementRequest(todoItem))
            }.await()

            result.handle(
                onSuccess = { data ->
                    revision = data.revision
                    loggSuccess(data)
                },
                onError = handleServerError,
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
            synchronizedNetworkRun {
                val result = async {
                    api.editItem(revision, createElementRequest(todoItem))
                }.await()

                result.handle(
                    onSuccess = { data ->
                        revision = data.revision
                        loggSuccess(data)
                    },
                    onError = handleServerError,
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
            synchronizedNetworkRun {
                val result = async { api.deleteItem(revision, id) }.await()
                result.handle(
                    onSuccess = { data ->
                        revision = data.revision
                        loggSuccess(data)
                    },
                    onError = handleServerError,
                )

            }
        }
    }

    override suspend fun getTodoItem(id: String): TodoItem? = withContext(defaultDispatcher) {
        _todoItems.value.firstOrNull { it.id == id }
    }

    override suspend fun clearErrorMessage() {
        errorMessage = null
    }
}