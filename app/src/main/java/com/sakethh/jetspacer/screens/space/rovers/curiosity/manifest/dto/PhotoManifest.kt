package com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PhotoManifest(
    @SerialName("landing_date")
    val landing_date: String,
    @SerialName("launch_date")
    val launch_date: String,
    @SerialName("max_date")
    val max_date: String,
    @SerialName("max_sol")
    val max_sol: Int,
    @SerialName("name")
    val name: String,
    @SerialName("photos")
    val photos: List<Photo>? = null,
    @SerialName("status")
    val status: String,
    @SerialName("total_photos")
    val total_photos: Int
)