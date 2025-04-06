package com.sakethh.jetspacer.domain.model.rover_latest_images

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoverLatestImagesDTO(
    @SerialName("latest_photos")
    val latestImages: List<LatestPhoto>
)