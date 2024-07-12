package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.data.db.AppDatabase
import com.example.todoapp.data.db.TodoItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Hilt module for providing Database dependencies. */

@Module
@InstallIn(SingletonComponent::class)
interface DataBaseModule {
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