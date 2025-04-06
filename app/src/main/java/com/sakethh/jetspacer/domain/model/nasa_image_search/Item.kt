package com.sakethh.jetspacer.domain.model.nasa_image_search

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val `data`: List<Data> = emptyList(),
    val href: String = "",
    val links: List<Link> = emptyList()
)