package com.sakethh.jetspacer.explore.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val href: String,
    val rel: String,
    val render: String
)