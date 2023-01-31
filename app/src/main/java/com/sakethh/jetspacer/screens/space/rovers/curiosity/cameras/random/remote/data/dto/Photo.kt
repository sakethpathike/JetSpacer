package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Photo(
    @SerialName("camera")
    val camera: Camera,
    @SerialName("earth_date")
    val earth_date: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("img_src")
    val img_src: String = "",
    @SerialName("rover")
    val rover: Rover,
    @SerialName("sol")
    val sol: Int = 0
)