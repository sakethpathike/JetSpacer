package com.sakethh.jetspacer.explore.domain.model.api

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val album: List<String> = emptyList(),
    val center: String = "",
    val date_created: String = "",
    val description: String = "",
    val description_508: String = "",
    val keywords: List<String> = emptyList(),
    val location: String = "",
    val media_type: String = "",
    val nasa_id: String = "",
    val photographer: String = "",
    val secondary_creator: String = "",
    val title: String = ""
)