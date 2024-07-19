package com.example.todoapp.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.domain.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/** Worker class for updating ToDoItems with network requests in the background. */

@HiltWorker
class NetworkUpdateTodoItemsWorker @AssistedInject constructor(
    private val repository: Repository,
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = try {
        repository.loadTodoItems()
        Result.success()
    } catch (error: Throwable) {
        Result.failure()
    }
}