package com.sakethh.jetspacer.ui.screens.home.state.apod

import androidx.compose.ui.graphics.Color

data class APODState(
    val isLoading: Boolean,
    val error: Boolean,
    val apod: Pair<ModifiedAPODDTO, List<Color>>,
    val statusCode: Int,
    val statusDescription: String
)
