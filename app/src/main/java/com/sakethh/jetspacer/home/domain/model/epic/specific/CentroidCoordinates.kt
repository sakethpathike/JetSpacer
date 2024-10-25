package com.sakethh.jetspacer.home.domain.model.epic.specific

import kotlinx.serialization.Serializable

@Serializable
data class CentroidCoordinates(
    val lat: Double,
    val lon: Double
)