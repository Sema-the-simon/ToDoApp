package com.example.todoapp.data.network.model

sealed class Result<out D : ResponseBody> {
    data class Success<out D : ResponseBody>(val data: D) : Result<D>()
    data class Error(val errorCode: Int = -1, val errorMessage: String) : Result<Nothing>()
}

inline fun <D : ResponseBody> Result<D>.handle(
    onSuccess: (D) -> Unit,
    onError: (Result.Error) -> Unit
) {
    when (this) {
        is Result.Success -> onSuccess(this.data)
        is Result.Error -> onError(this)
    }
}