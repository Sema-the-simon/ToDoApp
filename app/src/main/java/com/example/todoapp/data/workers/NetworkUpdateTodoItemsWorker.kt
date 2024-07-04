package com.example.todoapp.data.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.data.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException


@HiltWorker
class NetworkUpdateTodoItemsWorker @AssistedInject constructor(
    private val repository: Repository,
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = try {
        Log.d("networkTEST", "do Work")
        repository.loadTodoItems()
        Result.success()
    } catch (error: Throwable) {
        when (error) {
            is IOException -> Result.retry()
            else -> Result.failure()
        }
    }
}