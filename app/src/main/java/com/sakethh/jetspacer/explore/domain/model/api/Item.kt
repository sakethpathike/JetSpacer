package com.sakethh.jetspacer.explore.domain.model.api

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val `data`: List<Data> = emptyList(),
    val href: String = "",
    val links: List<Link> = emptyList()
)