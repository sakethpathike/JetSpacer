package com.sakethh.jetspacer.headlines.presentation

import com.sakethh.jetspacer.headlines.domain.model.NewsDTO

data class NewsScreenState(
    val isLoading: Boolean,
    val data: NewsDTO,
    val error: Boolean,
    val reachedMaxHeadlines: Boolean,
    val statusCode: Int,
    val statusDescription: String
)
