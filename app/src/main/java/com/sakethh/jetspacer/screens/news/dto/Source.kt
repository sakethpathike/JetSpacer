package com.sakethh.jetspacer.screens.news.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Source(
    @SerialName("id")
    val id: String="",
    @SerialName("name")
    val name: String=""
)