package com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@kotlinx.serialization.Serializable
data class Photo(
    @SerialName("cameras")
    val cameras: List<String>,
    @SerialName("earth_date")
    val earth_date: String,
    @SerialName("sol")
    val sol: Int,
    @SerialName("total_photos")
    val total_photos: Int
)