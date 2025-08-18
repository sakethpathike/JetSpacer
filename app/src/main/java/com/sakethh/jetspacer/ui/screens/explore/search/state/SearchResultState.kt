package com.sakethh.jetspacer.ui.screens.explore.search.state

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import com.sakethh.jetspacer.domain.model.NASAImageLibrarySearchDTOFlatten

typealias Index = Int

data class SearchResultState(
    val data: List<NASAImageLibrarySearchDTOFlatten>,
    val colors: MutableMap<Index, List<Color>> = mutableStateMapOf(),
    val isLoading: Boolean,
    val error: Boolean,
    val statusCode: Int,
    val statusDescription: String
)
