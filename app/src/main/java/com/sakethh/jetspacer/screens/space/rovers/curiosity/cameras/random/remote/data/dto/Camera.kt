package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto
@kotlinx.serialization.Serializable
data class Camera(
    val full_name: String="",
    val id: Int=0,
    val name: String="",
    val rover_id: Int=0
)