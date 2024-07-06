package com.example.todoapp.data.network.model

sealed class ResponseResult<out D : ResponseBody> {
    data class Success<out D : ResponseBody>(val data: D) : ResponseResult<D>()
    data class Error(val errorCode: Int = -1, val errorMessage: String) : ResponseResult<Nothing>()
}

inline fun <D : ResponseBody> ResponseResult<D>.handle(
    onSuccess: (D) -> Unit,
    onError: (ResponseResult.Error) -> Unit
) {
    when (this) {
        is ResponseResult.Success -> onSuccess(this.data)
        is ResponseResult.Error -> onError(this)
    }
}