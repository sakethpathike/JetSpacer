package com.sakethh.jetspacer.news.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val id: String = "",
    val name: String = ""
)