package com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class IssPosition(
    @SerialName("latitude")
    val latitude: String,
    @SerialName("longitude")
    val longitude: String
)