package com.sakethh.jetspacer.screens.news.dto

@kotlinx.serialization.Serializable
data class Article(
    val author: String ="",
    val content: String = "",
    val description: String ="",
    val publishedAt: String="",
    val source: Source,
    val title: String="",
    val url: String="",
    val urlToImage: String=""
)