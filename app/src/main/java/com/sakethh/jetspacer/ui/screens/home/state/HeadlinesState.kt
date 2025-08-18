package com.sakethh.jetspacer.ui.screens.home.state

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import com.sakethh.jetspacer.domain.model.headlines.Article
import com.sakethh.jetspacer.ui.screens.explore.search.state.Index

data class HeadlinesState(
    val isLoading: Boolean,
    val data: List<Article>,
    val colors: MutableMap<Index, List<Color>> = mutableStateMapOf(),
    val error: Boolean,
    val reachedMaxHeadlines: Boolean,
    val statusCode: Int,
    val statusDescription: String
)