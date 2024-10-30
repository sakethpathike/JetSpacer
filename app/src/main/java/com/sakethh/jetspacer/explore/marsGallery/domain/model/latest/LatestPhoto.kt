package com.sakethh.jetspacer.explore.marsGallery.domain.model.latest

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