package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto

@kotlinx.serialization.Serializable
data class Photo(
    val camera: Camera,
    val earth_date: String = "",
    val id: Int = 0,
    val img_src: String = "",
    val rover: Rover,
    val sol: Int = 0
)