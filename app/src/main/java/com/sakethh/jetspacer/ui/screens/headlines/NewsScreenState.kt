package com.sakethh.jetspacer.ui.screens.headlines

import com.sakethh.jetspacer.domain.model.Headline

data class NewsScreenState(
    val isLoading: Boolean,
    val data: List<Headline>,
    val error: Boolean,
    val reachedMaxHeadlines: Boolean,
    val statusCode: Int,
    val statusDescription: String
)