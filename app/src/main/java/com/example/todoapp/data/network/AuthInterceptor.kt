package com.example.todoapp.data.network

import io.ktor.http.HttpHeaders
import okhttp3.Interceptor
import okhttp3.Response

enum class TokenType {
    Bearer,
    OAuth
}

private const val TOKEN = TODO("Place your token here")
private val TYPE: String = TokenType.OAuth.name

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header(HttpHeaders.Authorization, "$TYPE $TOKEN").build()
        return chain.proceed(newRequest)
    }
}