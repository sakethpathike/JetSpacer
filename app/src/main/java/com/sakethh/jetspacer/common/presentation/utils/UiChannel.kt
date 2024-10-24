package com.sakethh.jetspacer.common.presentation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object UiChannel {

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun pushUiEvent(uiEvent: UIEvent, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}