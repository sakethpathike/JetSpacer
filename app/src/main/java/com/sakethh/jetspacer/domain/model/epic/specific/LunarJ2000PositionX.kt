package com.sakethh.jetspacer.domain.model.epic.specific

import kotlinx.serialization.Serializable

@Serializable
data class LunarJ2000PositionX(
    val x: Double,
    val y: Double,
    val z: Double
)