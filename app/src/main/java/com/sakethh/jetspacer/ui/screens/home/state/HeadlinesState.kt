package com.sakethh.jetspacer.ui.screens.home.state

import androidx.compose.ui.graphics.Color
import com.sakethh.jetspacer.domain.model.headlines.Article

data class HeadlinesState(
    val isLoading: Boolean,
    val data: List<Pair<Article, List<Color>>>,
    val error: Boolean,
    val reachedMaxHeadlines: Boolean,
    val statusCode: Int,
    val statusDescription: String
)