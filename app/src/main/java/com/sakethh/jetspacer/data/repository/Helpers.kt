package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.domain.Response
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

inline fun <reified T> extractBodyFlow(crossinline httpResponse: suspend () -> HttpResponse): Flow<Response<T>> {
    return flow<Response<T>> {
        emit(Response.Loading())
        val httpResponse = httpResponse()
        if (httpResponse.status.value != 200) {
            emit(
                Response.Failure(
                    statusCode = httpResponse.status.value,
                    statusDescription = httpResponse.status.description,
                    exceptionMessage = ""
                )
            )
            return@flow
        }
        emit(Response.Success(httpResponse.body<T>()))
    }.catch {
        emit(
            Response.Failure(
                statusCode = -1,
                statusDescription = it.message
                    ?: "Something went wrong, but the error didn't say what.",
                exceptionMessage = it.message.toString()
            )
        )
    }
}