package com.example.todoapp.data.network.interceptors

import io.ktor.http.HttpHeaders
import okhttp3.Interceptor
import okhttp3.Response

enum class TokenType {
    Bearer,
    OAuth
}

private const val TOKEN = TODO("PLACE YOUR TOKEN HERE")
private val TYPE: String = TokenType.OAuth.name

/** Intercepts HTTP requests to add an authorization header with a token. */

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header(HttpHeaders.Authorization, "$TYPE $TOKEN").build()
        return chain.proceed(newRequest)
    }
}