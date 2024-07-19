package com.example.todoapp.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/** Intercepts HTTP requests to add a header for generating 500-k server errors. */

private const val GENERATE_FAILS_HEADER = "X-Generate-Fails"
private const val THRESHOLD = 0

class ServerErrorGeneratorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header(GENERATE_FAILS_HEADER, "$THRESHOLD").build()
        return chain.proceed(newRequest)
    }
}