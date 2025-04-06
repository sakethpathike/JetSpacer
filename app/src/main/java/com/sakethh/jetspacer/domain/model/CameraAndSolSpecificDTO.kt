package com.sakethh.jetspacer.domain.model

import com.sakethh.jetspacer.domain.model.rover_latest_images.LatestPhoto
import kotlinx.serialization.Serializable

@Serializable
data class CameraAndSolSpecificDTO(
    val photos: List<LatestPhoto>
)