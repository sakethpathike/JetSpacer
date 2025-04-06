package com.sakethh.jetspacer.domain.model.article

import kotlinx.serialization.Serializable

@Serializable
data class NewsDTO(
    val articles: List<Article> = emptyList(),
    val status: String = "",
    val totalResults: Int = 0
)