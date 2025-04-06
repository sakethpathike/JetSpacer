package com.sakethh.jetspacer.ui.explore.apodArchive

import com.sakethh.jetspacer.ui.home.state.apod.ModifiedAPODDTO

data class APODArchiveScreenState(
    val data: List<ModifiedAPODDTO>,
    val isLoading: Boolean,
    val error: Boolean,
    val statusCode: Int,
    val statusDescription: String
)
