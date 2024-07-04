package com.example.todoapp.data

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest.Companion.MIN_BACKOFF_MILLIS
import com.example.todoapp.data.workers.NetworkUpdateTodoItemsWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val REFRESH_RATE_HOURS = 8L
private const val ON_START_UPDATE_WORKER_NAME = "OnStartUpdateWorker"
private const val TAG_UPDATE_TODO_ITEMS = "UpdateTodoItemsTag"

private const val PERIODIC_UPDATE_WORKER_NAME = "PeriodicUpdateWorker"
private const val TAG_PERIODIC_UPDATE_TODO_ITEMS = "PeriodicUpdateTodoItemsTag"

class TodoItemsDataSource @Inject constructor(
    private val workManager: WorkManager,
    private val connectivityManager: ConnectivityManager
) {
    private val networkConstraints
        get() = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    private val _internetConnectionState: MutableStateFlow<Boolean> =
        MutableStateFlow(isNetworkAvailable())
    val internetConnectionState = _internetConnectionState.asStateFlow()

    init {
        monitorNetworkConnection()
    }

    private fun isNetworkAvailable(): Boolean {
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    fun loadTodoItemsOnStart() {
        val request = OneTimeWorkRequestBuilder<NetworkUpdateTodoItemsWorker>()
            .setConstraints(networkConstraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag(TAG_UPDATE_TODO_ITEMS)
            .build()
        workManager.enqueueUniqueWork(
            ON_START_UPDATE_WORKER_NAME,
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    fun loadTodoItemsPeriodically() {
        val request = PeriodicWorkRequestBuilder<NetworkUpdateTodoItemsWorker>(
            REFRESH_RATE_HOURS, TimeUnit.HOURS
        )
            .setConstraints(networkConstraints)
            .addTag(TAG_PERIODIC_UPDATE_TODO_ITEMS)

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_UPDATE_WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request.build()
        )
    }

    private fun monitorNetworkConnection() {
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.d("networkTEST", "avaliable")
                _internetConnectionState.update {
                    true
                }
            }

            override fun onLost(network: Network) {
                Log.d("networkTEST", "lost")

                _internetConnectionState.update {
                    false
                }
            }
        })
    }

    fun stopLoadPeriodically() {
        workManager.cancelAllWorkByTag(TAG_UPDATE_TODO_ITEMS)
    }
}