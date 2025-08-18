package com.sakethh.jetspacer.domain.model.headlines

import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val id: String = "",
    val name: String = ""
)