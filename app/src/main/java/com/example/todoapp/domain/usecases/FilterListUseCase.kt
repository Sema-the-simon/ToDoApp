package com.example.todoapp.domain.usecases

import com.example.todoapp.data.datastore.PreferencesManager
import com.example.todoapp.domain.model.TodoItem
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Use case for filtering list [TodoItem] based on filter state from [PreferencesManager]. */

@Singleton
class FilterListUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {

    val isFiltered = preferencesManager.userPreferences.map { it.isListFilter }

    operator fun invoke(list: List<TodoItem>, isFiltered: Boolean): List<TodoItem> =
        if (isFiltered) list.filter { !it.isDone } else list


    suspend fun changeFilterState(isFiltered: Boolean) {
        preferencesManager.saveFilterState(isFiltered)
    }
}