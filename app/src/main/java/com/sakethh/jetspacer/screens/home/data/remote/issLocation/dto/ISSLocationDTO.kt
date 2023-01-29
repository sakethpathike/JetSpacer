package com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto

@kotlinx.serialization.Serializable
data class ISSLocationDTO(
    val iss_position: IssPosition,
    val message: String,
    val timestamp: Int
)