package com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ISSLocationDTO(
    @SerialName("iss_position")
    val iss_position: IssPosition,
    @SerialName("message")
    val message: String,
    @SerialName("timestamp")
    val timestamp: Int
)