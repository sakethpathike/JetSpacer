package com.sakethh.jetspacer.common.presentation.utils

sealed class UIEvent {
    data class ShowToast(val msg: String) : UIEvent()
    data object Nothing : UIEvent()
}