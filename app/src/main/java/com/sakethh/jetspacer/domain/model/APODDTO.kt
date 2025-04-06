package com.sakethh.jetspacer.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class APODDTO(
    val copyright: String?,
    val date: String?,
    val explanation: String?,
    @SerialName("hdurl")
    val hdUrl: String?,
    @SerialName("media_type")
    val mediaType: String?, // image
    val title: String?,
    val url: String?
)