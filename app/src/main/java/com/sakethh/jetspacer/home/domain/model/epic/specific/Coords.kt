package com.sakethh.jetspacer.home.domain.model.epic.specific

import kotlinx.serialization.Serializable

@Serializable
data class Coords(
    val attitude_quaternions: AttitudeQuaternions,
    val centroid_coordinates: CentroidCoordinates,
    val dscovr_j2000_position: DscovrJ2000PositionX,
    val lunar_j2000_position: LunarJ2000PositionX,
    val sun_j2000_position: SunJ2000PositionX
)