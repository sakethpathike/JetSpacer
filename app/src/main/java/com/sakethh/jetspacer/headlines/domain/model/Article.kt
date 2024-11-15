package com.sakethh.jetspacer.headlines.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val author: String = "",
    val content: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val source: Source,
    val title: String = "",
    val url: String = "",
    val urlToImage: String = ""
) {
    var isBookMarked = false
    var id: Long = -1
}