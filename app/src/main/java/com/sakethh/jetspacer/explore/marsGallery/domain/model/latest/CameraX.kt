package com.sakethh.jetspacer.explore.marsGallery.domain.model.latest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CameraX(
    @SerialName("full_name")
    val fullName: String,
    val name: String
)