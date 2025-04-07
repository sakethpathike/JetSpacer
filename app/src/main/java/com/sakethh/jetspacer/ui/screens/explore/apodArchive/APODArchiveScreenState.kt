package com.sakethh.jetspacer.ui.screens.explore.apodArchive

import com.sakethh.jetspacer.ui.screens.home.state.apod.ModifiedAPODDTO

data class APODArchiveScreenState(
    val data: List<ModifiedAPODDTO>,
    val isLoading: Boolean,
    val error: Boolean,
    val statusCode: Int,
    val statusDescription: String
)
