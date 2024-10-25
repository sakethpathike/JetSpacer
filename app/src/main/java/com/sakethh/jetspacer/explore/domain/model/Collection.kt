package com.sakethh.jetspacer.explore.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Collection(
    val href: String,
    val items: List<Item>,
    val links: List<LinkX>,
    val metadata: Metadata,
    val version: String
)