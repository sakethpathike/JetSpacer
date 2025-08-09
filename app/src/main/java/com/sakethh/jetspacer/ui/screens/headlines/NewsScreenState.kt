package com.sakethh.jetspacer.ui.screens.headlines

import androidx.compose.ui.graphics.Color
import com.sakethh.jetspacer.domain.model.Headline

data class NewsScreenState(
    val isLoading: Boolean,
    val data: List<Pair<Headline, List<Color>>>,
    val error: Boolean,
    val reachedMaxHeadlines: Boolean,
    val statusCode: Int,
    val statusDescription: String
)