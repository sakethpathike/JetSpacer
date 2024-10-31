package com.sakethh.jetspacer.common.presentation.utils.uiEvent

sealed class UIEvent {
    data class ShowSnackbar(val errorMessage: String) : UIEvent()
    data object Nothing : UIEvent()
}