package com.sakethh.jetspacer.explore.marsGallery.presentation.state

import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.RoverLatestImagesDTO
import kotlinx.serialization.Serializable

@Serializable
data class MarsGalleryLatestImagesState(
    val data: RoverLatestImagesDTO,
    val isLoading: Boolean,
    val error: Boolean,
    val roverName: String
)
