package com.sakethh.jetspacer.domain.model.rover_latest_images

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CameraX(
    @SerialName("full_name")
    val fullName: String,
    val name: String
)