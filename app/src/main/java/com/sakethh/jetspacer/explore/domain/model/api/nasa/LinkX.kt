package com.sakethh.jetspacer.explore.domain.model.api.nasa

import kotlinx.serialization.Serializable

@Serializable
data class LinkX(
    val href: String = "",
    val prompt: String = "",
    val rel: String = ""
)