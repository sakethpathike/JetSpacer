package com.sakethh.jetspacer.explore.domain.model.api

import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    val total_hits: Int = -1
)