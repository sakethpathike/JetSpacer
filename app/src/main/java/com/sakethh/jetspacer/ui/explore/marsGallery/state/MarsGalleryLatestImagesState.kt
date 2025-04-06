package com.sakethh.jetspacer.ui.explore.marsGallery.state

import com.sakethh.jetspacer.domain.model.rover_latest_images.RoverLatestImagesDTO
import kotlinx.serialization.Serializable

@Serializable
data class MarsGalleryLatestImagesState(
    val data: RoverLatestImagesDTO,
    val isLoading: Boolean,
    val error: Boolean,
    val roverName: String,
    val statusCode: Int,
    val statusDescription: String
)
