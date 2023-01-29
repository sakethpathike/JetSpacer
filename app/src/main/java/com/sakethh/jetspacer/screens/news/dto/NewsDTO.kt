package com.sakethh.jetspacer.screens.news.dto

@kotlinx.serialization.Serializable
data class NewsDTO(
    val articles: List<Article> = emptyList(),
    val status: String = "",
    val totalResults: Int = 0
)