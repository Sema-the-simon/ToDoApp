package com.example.todoapp.data.network.interceptors

import io.ktor.http.HttpHeaders
import okhttp3.Interceptor
import okhttp3.Response

enum class TokenType {
    Bearer,
    OAuth
}

private val TOKEN :String? = null
private val TYPE: String = TokenType.OAuth.name

/** Intercepts HTTP requests to add an authorization header with a token. */

class ServerAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = TOKEN ?: throw IllegalArgumentException("Place your token here in TOKEN constant in AuthInterceptor.kt")
        val newRequest = originalRequest.newBuilder()
            .header(HttpHeaders.Authorization, "$TYPE $TOKEN").build()
        return chain.proceed(newRequest)
    }
}