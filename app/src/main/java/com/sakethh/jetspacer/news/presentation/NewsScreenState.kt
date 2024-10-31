package com.sakethh.jetspacer.news.presentation

import com.sakethh.jetspacer.news.domain.model.NewsDTO

data class NewsScreenState(
    val isLoading: Boolean,
    val data: NewsDTO,
    val error: Boolean,
    val reachedMaxHeadlines: Boolean
)
