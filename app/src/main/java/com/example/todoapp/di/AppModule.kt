package com.example.todoapp.di

import com.example.todoapp.data.TodoItemsRepository
import com.example.todoapp.data.network.Api
import com.example.todoapp.data.network.ApiService
import com.example.todoapp.domain.Repository
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

    @Singleton
    @Binds
    fun provideApi(api: ApiService): Api

    companion object {

    }
}