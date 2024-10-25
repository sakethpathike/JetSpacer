package com.sakethh.jetspacer.explore.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val `data`: List<Data>,
    val href: String,
    val links: List<Link>
)