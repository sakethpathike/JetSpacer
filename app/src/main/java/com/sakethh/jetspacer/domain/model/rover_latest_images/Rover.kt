package com.sakethh.jetspacer.domain.model.rover_latest_images

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rover(
    val cameras: List<CameraX> = emptyList(), val id: Int = 0,
    @SerialName("landing_date") val landingDate: String = "",
    @SerialName("launch_date") val launchDate: String = "",
    @SerialName("max_date") val maxDate: String = "",
    @SerialName("max_sol") val maxSol: Int = 0, val name: String = "", val status: String = "",
    @SerialName("total_photos") val totalImages: Int = 0
)