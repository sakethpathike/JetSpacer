package com.sakethh.jetspacer.explore.domain.model.api.nasa

import kotlinx.serialization.Serializable

@Serializable
data class Collection(
    val href: String = "",
    val items: List<Item> = emptyList(),
    val links: List<LinkX> = emptyList(),
    val metadata: Metadata = Metadata(),
    val version: String = ""
)