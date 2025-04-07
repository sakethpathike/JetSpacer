package com.sakethh.jetspacer.ui.screens.explore.search.state

import com.sakethh.jetspacer.domain.model.NASAImageLibrarySearchModifiedDTO

data class SearchResultState(
    val data: List<NASAImageLibrarySearchModifiedDTO>,
    val isLoading: Boolean,
    val error: Boolean,
    val statusCode: Int,
    val statusDescription: String
)
