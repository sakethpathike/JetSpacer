package com.sakethh.jetspacer.domain

sealed interface Response<T> {
    data class Success<T>(val data: T) : Response<T>
    data class Failure<T>(
        val exceptionMessage: String, val statusCode: Int, val statusDescription: String
    ) : Response<T> {
        companion object {
            fun <T> clientFailure(exceptionMessage: String = "Failed to retrieve data, servers might be down."): Failure<T> {
                return Failure(
                    exceptionMessage = exceptionMessage,
                    statusCode = 0,
                    statusDescription = "Client Failure"
                )
            }
        }
    }

    data class Loading<T>(val msg: String = "") : Response<T>
}

suspend fun <T> Response<T>.onSuccess(init: suspend (T) -> Unit): Response<T> {
    if (this is Response.Success) {
        init(this.data)
    }
    return this
}

fun <T> Response<T>.onFailure(init: (Response.Failure<T>) -> Unit): Response<T> {
    if (this is Response.Failure) {
        init(this)
    }
    return this
}

fun <T> Response<T>.onLoading(init: () -> Unit): Response<T> {
    if (this is Response.Loading) {
        init()
    }
    return this
}