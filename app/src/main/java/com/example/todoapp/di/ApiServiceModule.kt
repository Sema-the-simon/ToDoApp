package com.example.todoapp.di

import com.example.todoapp.data.network.interceptors.AuthInterceptor
import com.example.todoapp.data.network.interceptors.ServerErrorGeneratorInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/** Hilt module for providing ApiService dependencies. */

@Module
@InstallIn(SingletonComponent::class)
interface ApiServiceModule {

    companion object {
        @Singleton
        @Provides
        fun provideHttpClient(
            okHttpClient: OkHttpClient
        ): HttpClient {
            return HttpClient(OkHttp) {
                engine {
                    preconfigured = okHttpClient
                }
                followRedirects = false

                install(ContentNegotiation) {
                    json()
                }
                install(Logging)
                install(HttpRequestRetry) {
                    retryOnServerErrors(maxRetries = 3)
                    exponentialDelay()
                }
            }
        }

        @Singleton
        @Provides
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(AuthInterceptor())
                .addInterceptor(ServerErrorGeneratorInterceptor())
                .build()
        }
    }
}