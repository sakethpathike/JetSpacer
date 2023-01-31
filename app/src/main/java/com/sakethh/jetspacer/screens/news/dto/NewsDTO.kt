package com.sakethh.jetspacer.screens.news.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class NewsDTO(
    @SerialName("urlToImage")
    val articles: List<Article> = emptyList(),
    @SerialName("status")
    val status: String = "",
    @SerialName("totalResults")
    val totalResults: Int = 0
)