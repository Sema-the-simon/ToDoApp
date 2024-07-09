package com.example.todoapp.data.network.model

/** Represents the result of a network call, encapsulating [ResponseBody]
 * either [Success] with data or an [Error] with a message and error code.
 * Here also extension function [handle] to handle ResponseResult with success and error callbacks. */

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