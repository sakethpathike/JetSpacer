package com.sakethh.jetspacer.screens.news.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Article(
    @SerialName("author")
    val author: String ="",
    @SerialName("content")
    val content: String = "",
    @SerialName("description")
    val description: String ="",
    @SerialName("publishedAt")
    val publishedAt: String="",
    @SerialName("source")
    val source: Source,
    @SerialName("title")
    val title: String="",
    @SerialName("url")
    val url: String="",
    @SerialName("urlToImage")
    val urlToImage: String=""
)