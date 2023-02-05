package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@kotlinx.serialization.Serializable
data class RandomCameraDTO(
    @SerialName("photos")
    val photos: List<Photo>
)