package com.sakethh.jetspacer.explore.domain.model.api.iss.source

import kotlinx.serialization.Serializable

@Serializable
data class IssPosition(
    val latitude: String,
    val longitude: String
)