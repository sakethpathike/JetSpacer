package com.sakethh.jetspacer.screens.news.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@kotlinx.serialization.Serializable
data class NewsDTO(
    @SerialName("articles")
    val articles: List<Article> = emptyList(),
    @SerialName("status")
    val status: String = "",
    @SerialName("totalResults")
    val totalResults: Int = 0
)