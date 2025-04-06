package com.sakethh.jetspacer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NASAImageLibrarySearchModifiedDTO(
    val keywords: List<String>,
    val dateCreated: String,
    val location: String,
    val photographer: String,
    val title: String,
    val description: String,
    val nasaId: String,
    val imageUrl: String
)