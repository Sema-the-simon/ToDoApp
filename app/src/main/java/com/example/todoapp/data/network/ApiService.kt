package com.example.todoapp.data.network

import com.example.todoapp.data.network.model.RequestBody
import com.example.todoapp.data.network.model.ResponseBody
import com.example.todoapp.data.network.model.Result
import com.example.todoapp.di.IoDispatcher
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val BASE_URL = "https://hive.mrdekk.ru/todo"
private const val REVISION_HEADER = "X-Last-Known-Revision"
private const val GENERATE_FAILS_HEADER = "X-Generate-Fails"

class ApiService @Inject constructor(
    private val client: HttpClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : Api {

    private suspend fun <T : ResponseBody> getResult(block: suspend CoroutineScope.() -> T): Result<T> =
        withContext(ioDispatcher) {
            try {
                val body: T = block()
                Result.Success(body)
            } catch (e: Exception) {
                handleError(e)
            }
        }

    private fun <E> handleError(e: Exception): Result.Error<E> {
        return when (e) {
            is CancellationException -> throw e
            is ResponseException -> {
                Result.Error(
                    errorMessage = e.message ?: "network error",
                    errorCode = e.response.status.value
                )
            }

            else -> {
                Result.Error(errorMessage = "${e.javaClass} ${e.message}")
            }
        }
    }

    override suspend fun getTodoList(): Result<ResponseBody.TodoList> =
        getResult {
            val response: HttpResponse = client
                .get(BASE_URL) {
                    url {
                        appendPathSegments("list")
                    }
                    //header(GENERATE_FAILS_HEADER, 70)
                    expectSuccess = true
                }
            response.body()
        }


    override suspend fun updateList(
        revision: Int,
        req: RequestBody.TodoList
    ): Result<ResponseBody.TodoList> =
        getResult {
            val response: HttpResponse = client
                .patch(BASE_URL) {
                    url {
                        appendPathSegments("list")
                    }
                    header(REVISION_HEADER, revision)
                    contentType(ContentType.Application.Json)
                    setBody(req)
                    expectSuccess = true
                }
            response.body()
        }

    override suspend fun getItemById(revision: Int, id: String): Result<ResponseBody.TodoElement> =
        getResult {
            val response: HttpResponse = client
                .get(BASE_URL) {
                    url {
                        appendPathSegments("list", id)
                    }
                    expectSuccess = true
                }
            response.body()
        }

    override suspend fun addItem(
        revision: Int,
        req: RequestBody.TodoElement
    ): Result<ResponseBody.TodoElement> =
        getResult {
            val response: HttpResponse = client
                .post(BASE_URL) {
                    url {
                        appendPathSegments("list")
                    }
                    header(REVISION_HEADER, revision)
                    contentType(ContentType.Application.Json)
                    setBody(req)
                    expectSuccess = true
                }
            response.body()
        }

    override suspend fun editItem(
        revision: Int,
        req: RequestBody.TodoElement
    ): Result<ResponseBody.TodoElement> =
        getResult {
            val response: HttpResponse = client
                .put(BASE_URL) {
                    url {
                        appendPathSegments("list", req.element.id)
                    }
                    header(REVISION_HEADER, revision)
                    contentType(ContentType.Application.Json)
                    setBody(req)
                    expectSuccess = true
                }
            response.body()
        }

    override suspend fun deleteItem(revision: Int, id: String): Result<ResponseBody.TodoElement> =
        getResult {
            val response: HttpResponse = client
                .delete(BASE_URL) {
                    url {
                        appendPathSegments("list", id)
                    }
                    expectSuccess = true
                }
            response.body()
        }


}