package com.example.todoapp.data.network

import io.ktor.http.HttpHeaders
import okhttp3.Interceptor
import okhttp3.Response

private const val TOKEN = "y0_AgAAAAAelQmIAARC0QAAAAEJEQgMAACvplsEl-pH5pna1tIZFabghMyJhQ"

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header(HttpHeaders.Authorization, "OAuth $TOKEN").build()
        return chain.proceed(newRequest)
    }
}