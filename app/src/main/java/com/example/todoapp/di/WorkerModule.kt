package com.example.todoapp.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.work.WorkManager
import com.example.todoapp.data.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Hilt module for providing Worker dependencies. */

@Module
@InstallIn(SingletonComponent::class)
interface WorkerModule {
    companion object {

        @Singleton
        @Provides
        fun provideNetworkManager(
            connectivityManager: ConnectivityManager,
            workManager: WorkManager
        ): NetworkManager {
            return NetworkManager(workManager, connectivityManager)
        }


        @Singleton
        @Provides
        fun provideWorkManagerInstance(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }

        @Singleton
        @Provides
        fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
            return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
    }
}