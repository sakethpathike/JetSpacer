package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@kotlinx.serialization.Serializable
data class Rover(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("landing_date")
    val landing_date: String = "",
    @SerialName("launch_date")
    val launch_date: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("status")
    val status: String = ""
)