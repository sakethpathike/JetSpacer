package com.sakethh.jetspacer.explore.apodArchive.presentation

import com.sakethh.jetspacer.home.presentation.state.apod.ModifiedAPODDTO

data class APODArchiveScreenState(
    val data: List<ModifiedAPODDTO>,
    val isLoading: Boolean,
    val error: Boolean
)
