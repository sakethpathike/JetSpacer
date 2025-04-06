package com.sakethh.jetspacer.domain.model.nasa_image_search

import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    val total_hits: Int = -1
)