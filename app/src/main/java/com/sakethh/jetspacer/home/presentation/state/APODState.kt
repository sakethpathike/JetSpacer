package com.sakethh.jetspacer.home.presentation.state

import com.sakethh.jetspacer.home.domain.model.APODDTO

data class APODState(
    val isLoading: Boolean,
    val error: Boolean,
    val apod: APODDTO
)
