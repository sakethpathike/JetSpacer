package com.sakethh.jetspacer.explore.marsGallery.domain.model.latest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rover(
    val cameras: List<CameraX>,
    val id: Int,
    @SerialName("landing_date")
    val landingDate: String,
    @SerialName("launch_date")
    val launchDate: String,
    @SerialName("max_date")
    val maxDate: String,
    @SerialName("max_sol")
    val maxSol: Int,
    val name: String,
    val status: String,
    @SerialName("total_photos")
    val totalImages: Int
)