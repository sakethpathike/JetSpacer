package com.sakethh.jetspacer.ui.home.state.apod

data class APODState(
    val isLoading: Boolean,
    val error: Boolean,
    val apod: ModifiedAPODDTO,
    val statusCode: Int,
    val statusDescription: String
)
