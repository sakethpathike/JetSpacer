package com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.dto

@kotlinx.serialization.Serializable
data class PhotoManifest(
    val landing_date: String,
    val launch_date: String,
    val max_date: String,
    val max_sol: Int,
    val name: String,
    val photos: List<Photo>? = null,
    val status: String,
    val total_photos: Int
)