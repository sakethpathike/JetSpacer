package com.sakethh.jetspacer.domain.model.headlines

import kotlinx.serialization.Serializable

@Serializable
data class HeadlinesDTO(
    val articles: List<Article> = emptyList(),
    val status: String = "",
    val totalResults: Int = 0
)