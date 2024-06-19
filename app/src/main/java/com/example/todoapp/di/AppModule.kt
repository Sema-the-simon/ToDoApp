package com.example.todoapp.di

import com.example.todoapp.data.Repository
import com.example.todoapp.data.TodoItemsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Singleton
    @Binds
    fun provideRepository(repository: TodoItemsRepository): Repository

    companion object {

    }
}