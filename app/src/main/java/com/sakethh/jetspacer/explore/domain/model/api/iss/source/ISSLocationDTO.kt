package com.sakethh.jetspacer.explore.domain.model.api.iss.source

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ISSLocationDTO(
    @SerialName("iss_position")
    val issPosition: IssPosition,
    val message: String,
    val timestamp: Int
)