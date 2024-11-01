package com.sakethh.jetspacer.common.network

sealed interface NetworkState<T> {
    data class Success<T>(val data: T) : NetworkState<T>
    data class Failure<T>(
        val exceptionMessage: String,
        val statusCode: Int,
        val statusDescription: String
    ) : NetworkState<T>
    data class Loading<T>(val msg: String = "") : NetworkState<T>
}