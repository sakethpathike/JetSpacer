package com.sakethh.jetspacer.ui.utils.uiEvent

sealed class UIEvent {
    data class ShowSnackbar(val errorMessage: String) : UIEvent()
    data object Nothing : UIEvent()
}