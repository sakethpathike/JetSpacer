package com.sakethh.jetspacer.explore.marsGallery.domain.model

import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.LatestPhoto
import kotlinx.serialization.Serializable

@Serializable
data class CameraAndSolSpecificDTO(
    val photos: List<LatestPhoto>
)