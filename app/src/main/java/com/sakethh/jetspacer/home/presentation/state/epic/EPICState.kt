package com.sakethh.jetspacer.home.presentation.state.epic

data class EPICState(
    val data: List<EpicStateItem>,
    val isLoading: Boolean,
    val error: Boolean,
    val statusCode: Int,
    val statusDescription: String
)
