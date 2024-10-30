package com.sakethh.jetspacer.explore.marsGallery.domain.model.latest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Camera(
    @SerialName("full_name")
    val fullName: String,
    val id: Int,
    val name: String,
    @SerialName("rover_id")
    val roverID: Int
)