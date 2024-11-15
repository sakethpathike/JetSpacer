package com.sakethh.jetspacer.headlines.presentation

import com.sakethh.jetspacer.common.data.local.domain.model.Headline

data class NewsScreenState(
    val isLoading: Boolean,
    val data: List<Headline>,
    val error: Boolean,
    val reachedMaxHeadlines: Boolean,
    val statusCode: Int,
    val statusDescription: String
)