package com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@kotlinx.serialization.Serializable
data class RoverManifestDTO(
    @SerialName("photo_manifest")
    val photo_manifest: PhotoManifest
)