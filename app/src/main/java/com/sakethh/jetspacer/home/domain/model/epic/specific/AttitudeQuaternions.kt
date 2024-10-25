package com.sakethh.jetspacer.home.domain.model.epic.specific

import kotlinx.serialization.Serializable

@Serializable
data class AttitudeQuaternions(
    val q0: Double,
    val q1: Double,
    val q2: Double,
    val q3: Double
)