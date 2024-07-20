package com.example.todoapp.data.network.interceptors

import android.util.Log
import com.example.todoapp.BuildConfig
import io.ktor.http.HttpHeaders
import okhttp3.Interceptor
import okhttp3.Response

enum class TokenType {
    Bearer,
    OAuth
}

private val TOKEN: String? =
    if (BuildConfig.DEBUG)
        null // put your token here
    else System.getenv("API_TOKEN")
private val TYPE: String = TokenType.OAuth.name

/** Intercepts HTTP requests to add an authorization header with a token. */

class ServerAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        Log.d("API_TOKEN", "currentBuildConfig is debug :${BuildConfig.DEBUG}")
        Log.d("API_TOKEN", "currentApiToken is :${System.getenv("API_TOKEN")}")

        val token = TOKEN
            ?: throw IllegalArgumentException("Place your token here in TOKEN constant in ServerAuthInterceptor.kt")
        val newRequest = originalRequest.newBuilder()
            .header(HttpHeaders.Authorization, "$TYPE $token").build()
        return chain.proceed(newRequest)
    }
}