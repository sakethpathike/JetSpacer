package com.sakethh.jetspacer.explore.domain.model.api

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val href: String = "",
    val rel: String = "",
    val render: String = ""
)