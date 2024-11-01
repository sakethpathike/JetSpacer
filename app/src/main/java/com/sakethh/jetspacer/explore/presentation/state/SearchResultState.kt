package com.sakethh.jetspacer.explore.presentation.state

import com.sakethh.jetspacer.explore.domain.model.local.NASAImageLibrarySearchModifiedDTO

data class SearchResultState(
    val data: List<NASAImageLibrarySearchModifiedDTO>,
    val isLoading: Boolean,
    val error: Boolean,
    val statusCode: Int,
    val statusDescription: String
)
