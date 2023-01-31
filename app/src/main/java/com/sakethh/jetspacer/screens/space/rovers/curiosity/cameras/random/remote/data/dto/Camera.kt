package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Camera(
    @SerialName("full_name")
    val full_name: String="",
    @SerialName("id")
    val id: Int=0,
    @SerialName("name")
    val name: String="",
    @SerialName("rover_id")
    val rover_id: Int=0
)