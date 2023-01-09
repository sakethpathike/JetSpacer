package com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.dto

@kotlinx.serialization.Serializable
data class Photo(
    val cameras: List<String>,
    val earth_date: String,
    val sol: Int,
    val total_photos: Int
)