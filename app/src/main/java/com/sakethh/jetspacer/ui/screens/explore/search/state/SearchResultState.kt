package com.sakethh.jetspacer.ui.screens.explore.search.state

import androidx.compose.ui.graphics.Color
import com.sakethh.jetspacer.domain.model.NASAImageLibrarySearchDTOFlatten

data class SearchResultState(
    val data: List<Pair<NASAImageLibrarySearchDTOFlatten, List<Color>>>,
    val isLoading: Boolean,
    val error: Boolean,
    val statusCode: Int,
    val statusDescription: String
)
