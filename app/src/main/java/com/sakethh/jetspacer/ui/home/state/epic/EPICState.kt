package com.sakethh.jetspacer.ui.home.state.epic

data class EPICState(
    val data: List<EpicStateItem>,
    val isLoading: Boolean,
    val error: Boolean,
    val statusCode: Int,
    val statusDescription: String
)
