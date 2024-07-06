package com.example.todoapp.domain.model

import com.example.todoapp.R

sealed class UserError {
    data object Unexpected : UserError() {
        override fun toStringResource(): Int = R.string.unexpected_error
    }

    data object NoInternetConnection : UserError() {
        override fun toStringResource(): Int = R.string.no_internet_connection
    }

    data object InternalServerError : UserError() {
        override fun toStringResource(): Int = R.string.error_500
    }

    data object UnsynchronizedData : UserError() {
        override fun toStringResource(): Int = R.string.error_400
    }

    data object Unauthorized : UserError() {
        override fun toStringResource(): Int = R.string.error_401
    }

    data object NotFound : UserError() {
        override fun toStringResource(): Int = R.string.error_404
    }

    abstract fun toStringResource(): Int
}