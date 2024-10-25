package com.sakethh.jetspacer.home.presentation.state

import com.sakethh.jetspacer.home.domain.model.epic.specific.EPICSpecificDTO

data class EPICState(
    val data: List<EPICSpecificDTO>,
    val isLoading: Boolean,
    val error: Boolean
)
