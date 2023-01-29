package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto

@kotlinx.serialization.Serializable
data class Rover(
    val id: Int = 0,
    val landing_date: String = "",
    val launch_date: String = "",
    val name: String = "",
    val status: String = ""
)