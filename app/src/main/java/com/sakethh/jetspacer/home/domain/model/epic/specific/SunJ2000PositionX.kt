package com.sakethh.jetspacer.home.domain.model.epic.specific

import kotlinx.serialization.Serializable

@Serializable
data class SunJ2000PositionX(
    val x: Double,
    val y: Double,
    val z: Double
)