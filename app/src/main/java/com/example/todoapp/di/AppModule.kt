package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.data.TodoItemsRepository
import com.example.todoapp.data.db.AppDatabase
import com.example.todoapp.data.db.TodoItemDao
import com.example.todoapp.data.network.Api
import com.example.todoapp.data.network.ApiService
import com.example.todoapp.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Hilt module for providing main app dependencies. */

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Singleton
    @Binds
    fun provideRepository(repository: TodoItemsRepository): Repository

    @Singleton
    @Binds
    fun provideApi(api: ApiService): Api

    companion object {

        @Singleton
        @Provides
        fun provideTodoItemDao(database: AppDatabase): TodoItemDao {
            return database.getTodoItemDao()
        }

        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
            return AppDatabase.getDatabaseInstance(context)
        }
    }
}