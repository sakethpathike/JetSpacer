package com.sakethh.jetspacer.ui.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object UIChannel {

    private val channel = Channel<Type>()
    val readChannel = channel.receiveAsFlow()

    fun push(type: Type, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            channel.send(type)
        }
    }

    sealed class Type {
        data class ShowSnackbar(val errorMessage: String) : Type()
        data object Nothing : Type()
    }
}