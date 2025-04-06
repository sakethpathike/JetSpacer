package com.sakethh.jetspacer.domain.model.rover_latest_images

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LatestPhoto(
    val camera: Camera,
    @SerialName("earth_date")
    val earthDate: String,
    val id: Int,
    @SerialName("img_src")
    val imgSrc: String,
    val rover: Rover,
    val sol: Int
)