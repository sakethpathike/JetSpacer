package com.sakethh.jetspacer.domain.model.iss.source

import kotlinx.serialization.Serializable

@Serializable
data class IssPosition(
    val latitude: String,
    val longitude: String
)