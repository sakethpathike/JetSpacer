package com.sakethh.jetspacer.domain.model.nasa_image_search

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val href: String = "",
    val rel: String = "",
    val render: String = ""
)