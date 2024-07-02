package com.example.todoapp.data.network.model

sealed interface Result<D> {
    data class Success<D : ResponseBody>(val data: D) :
        Result<D>

    data class Error<D>(
        val errorCode: Int = -1,
        val errorMessage: String
    ) :
        Result<D>
}