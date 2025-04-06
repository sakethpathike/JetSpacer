package com.sakethh.jetspacer.domain.model.nasa_image_search

import kotlinx.serialization.Serializable

@Serializable
data class LinkX(
    val href: String = "",
    val prompt: String = "",
    val rel: String = ""
)