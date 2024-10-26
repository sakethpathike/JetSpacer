package com.sakethh.jetspacer.home.presentation.state.apod

data class APODState(
    val isLoading: Boolean,
    val error: Boolean,
    val apod: ModifiedAPODDTO
)
