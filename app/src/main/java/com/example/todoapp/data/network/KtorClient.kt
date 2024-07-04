package com.example.todoapp.data.network

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.todoapp.data.workers.NetworkUpdateTodoItemsWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun getListFromServer(context: Context) = withContext(Dispatchers.IO) {
    val uploadWorkRequest: WorkRequest =
        OneTimeWorkRequestBuilder<NetworkUpdateTodoItemsWorker>()
            .build()

    WorkManager
        .getInstance(context)
        .enqueue(uploadWorkRequest)
}


