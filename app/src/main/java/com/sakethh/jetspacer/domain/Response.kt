package com.sakethh.jetspacer.domain

sealed interface Response<T> {
    data class Success<T>(val data: T) : Response<T>
    data class Failure<T>(
        val exceptionMessage: String,
        val statusCode: Int,
        val statusDescription: String
    ) : Response<T>

    data class Loading<T>(val msg: String = "") : Response<T>
}