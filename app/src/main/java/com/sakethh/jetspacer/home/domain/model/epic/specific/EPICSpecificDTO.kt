package com.sakethh.jetspacer.home.domain.model.epic.specific

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EPICSpecificDTO(
    @SerialName("attitude_quaternions")
    val satelliteAttitude: AttitudeQuaternions,
    val caption: String,
    @SerialName("centroid_coordinates")
    val coordinatesThatTheSatelliteIsLookingAt: CentroidCoordinates,
    val date: String,
    @SerialName("dscovr_j2000_position")
    val positionOfTheSatelliteInSpace: DscovrJ2000PositionX,
    val identifier: String,
    val image: String,
    @SerialName("lunar_j2000_position")
    val positionOfTheMoonInSpace: LunarJ2000PositionX,
    @SerialName("sun_j2000_position")
    val positionOfTheSunInSpace: SunJ2000PositionX,
    val version: String
)