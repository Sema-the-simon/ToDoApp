package com.example.todoapp.domain.usecases

import com.example.todoapp.data.datastore.DataStoreManager
import com.example.todoapp.domain.model.TodoItem
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Use case for filtering list [TodoItem] based on filter state from [DataStoreManager]. */

@Singleton
class FilterListUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    val isFiltered = dataStoreManager.userPreferences.map { it.isListFilter }

    operator fun invoke(list: List<TodoItem>, isFiltered: Boolean): List<TodoItem> =
        if (isFiltered) list.filter { !it.isDone } else list


    suspend fun changeFilterState(isFiltered: Boolean) {
        dataStoreManager.saveFilterState(isFiltered)
    }
}