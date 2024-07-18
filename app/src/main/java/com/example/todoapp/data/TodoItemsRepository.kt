package com.example.todoapp.data

import com.example.todoapp.data.datastore.PreferencesManager
import com.example.todoapp.data.db.TodoItemDao
import com.example.todoapp.data.db.TodoItemInfoTuple
import com.example.todoapp.data.db.entities.toEntity
import com.example.todoapp.data.network.Api
import com.example.todoapp.data.network.model.RequestBody
import com.example.todoapp.data.network.model.ResponseResult
import com.example.todoapp.data.network.model.asDto
import com.example.todoapp.data.network.model.handle
import com.example.todoapp.data.network.model.toTodoItem
import com.example.todoapp.di.ApplicationScope
import com.example.todoapp.di.DefaultDispatcher
import com.example.todoapp.domain.Repository
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.model.UserError
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** Repository for managing ToDoItems, handling data synchronization and error states.
 *  Work with remote and local data source - TODO(local data source not implemented yet) */

class TodoItemsRepository @Inject constructor(
    networkManager: NetworkManager,
    private val api: Api,
    private val todoItemDao: TodoItemDao,
    private val preferencesManager: PreferencesManager,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    @ApplicationScope
    private val externalScope: CoroutineScope,
) : Repository {
    private val databaseData: Flow<List<TodoItemInfoTuple>> = todoItemDao.observeTodoData()

    override val todoItems = databaseData.mapToTodoItemFlow()
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private fun Flow<List<TodoItemInfoTuple>>.mapToTodoItemFlow() =
        this.map { list -> list.filter { !it.isDeleted }.map { it.toTodoItem() } }


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

    private val isNetworkAvailable = networkManager.internetConnectionState

    private val revisionMutex = Mutex()
    private var revision = 0

    private var userId = ""

    private fun getUserId() = externalScope.launch {
        preferencesManager.userPreferences.collectLatest { pref ->
            userId = pref.userId
        }
    }

    init {
        getUserId()
        networkManager.loadTodoItemsOnce()
        networkManager.loadTodoItemsPeriodically()
    }

    override suspend fun countDoneTodos(): Int = todoItems.value.count { it.isDone }

    private suspend fun synchronizedNetworkRun(action: suspend CoroutineScope.() -> Unit) {
        if (isNetworkAvailable.value) {
            externalScope.launch {
                revisionMutex.withLock {
                    action()
                }
            }
        } else {
            if (isDataLoading) {
                delay(100)
                isDataLoading = false
                errorMessage = UserError.NoInternetConnection
            }
            isDataSynchronized = false
        }
    }

    private val handleServerError: (error: ResponseResult.Error) -> Unit = { error ->
        errorMessage = when (error.errorCode) {
            HttpStatusCode.InternalServerError.value -> UserError.InternalServerError
            HttpStatusCode.BadRequest.value -> UserError.UnsynchronizedData
            HttpStatusCode.Unauthorized.value -> UserError.Unauthorized
            HttpStatusCode.NotFound.value -> UserError.NotFound
            else -> UserError.Unexpected
        }
        isDataLoading = false
        isDataSynchronized = false
    }

    private fun createElementRequest(element: TodoItem): RequestBody.TodoElement =
        RequestBody.TodoElement(element.asDto(userId))

    private fun createListRequest(list: List<TodoItem>): RequestBody.TodoList =
        RequestBody.TodoList(list.map { it.asDto(userId) })

    override suspend fun loadTodoItems() = withContext(defaultDispatcher) {
        isDataLoading = true
        synchronizedNetworkRun {
            val result = api.getTodoList()
            result.handle(
                onSuccess = { data ->
                    val newElements = data.list.map { it.toTodoItem() }
                    revision = data.revision
                    updateTodoItemsInDataBase(newElements)
                    isDataLoading = false
                    syncWithServer()
                    isDataSynchronized = true
                },
                onError = handleServerError,
            )
        }
    }

    private suspend fun updateTodoItemsInDataBase(newItems: List<TodoItem>) {
        val currentItems = hashMapOf<String, TodoItemInfoTuple>()
        todoItemDao.getAllTodoData().forEach { el ->
            currentItems[el.id] = el
        }
        for (newEl in newItems) {
            if (newEl.id in currentItems.keys) {
                val curEl = currentItems[newEl.id]!!
                if (curEl.isDeleted) {
                    todoItemDao.deleteTodoDataById(curEl.id)
                    continue
                }
                val serverModification = newEl.modificationDate ?: 0L
                val databaseModification = curEl.changedAt
                if (newEl != currentItems[newEl.id]!!.toTodoItem() && serverModification >= databaseModification) {
                    todoItemDao.updateTodoData(newEl.toEntity())
                } else
                    continue
            } else (newEl.id !in currentItems.keys)
            todoItemDao.insertNewTodoItemData(newEl.toEntity())
        }
        todoItemDao.clearDeleted()
    }

    private suspend fun syncWithServer() {
        synchronizedNetworkRun {
            val result = api.updateList(revision, createListRequest(todoItems.value))
            result.handle(
                onSuccess = { data -> revision = data.revision },
                onError = handleServerError,
            )
        }
    }

    override suspend fun addTodoItem(todoItem: TodoItem) = withContext(defaultDispatcher) {
        todoItemDao.insertNewTodoItemData(todoItem.toEntity())
        synchronizedNetworkRun {
            val result = api.addItem(revision, createElementRequest(todoItem))

            result.handle(
                onSuccess = { data -> revision = data.revision },
                onError = handleServerError,
            )
        }
    }

    override suspend fun updateItem(todoItem: TodoItem) = withContext(defaultDispatcher) {
        todoItemDao.updateTodoData(
            todoItem.copy(modificationDate = System.currentTimeMillis()).toEntity()
        )
        synchronizedNetworkRun {
            val result = api.editItem(revision, createElementRequest(todoItem))

            result.handle(
                onSuccess = { data -> revision = data.revision },
                onError = handleServerError,
            )
        }
    }

    override suspend fun removeItem(id: String) = withContext(defaultDispatcher) {
        val item = getTodoItem(id)!!
        todoItemDao.updateTodoData(item.toEntity(isDeleted = true))
        synchronizedNetworkRun {
            val result = api.deleteItem(revision, id)
            result.handle(
                onSuccess = { data ->
                    revision = data.revision
                    todoItemDao.deleteTodoDataById(id)
                },
                onError = handleServerError,
            )
        }
    }

    override suspend fun removeItems(ids: List<String>) = withContext(defaultDispatcher) {
        val items = ids.map { id -> getTodoItem(id)!! }
        items.forEach { item -> todoItemDao.updateTodoData(item.toEntity(isDeleted = true)) }
        synchronizedNetworkRun {
            val newElements = todoItems.value.toMutableList().also { it.removeAll(items) }
            val result = api.updateList(revision, createListRequest(newElements))
            result.handle(
                onSuccess = { data ->
                    revision = data.revision
                    items.forEach { todoItemDao.deleteTodoDataById(it.id) }
                },
                onError = handleServerError,
            )
        }
    }

    override suspend fun getTodoItem(id: String): TodoItem? = withContext(defaultDispatcher) {
        todoItems.value.firstOrNull { it.id == id }
    }

    override suspend fun clearErrorMessage() {
        errorMessage = null
    }
}