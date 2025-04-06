package com.sakethh.jetspacer.domain.model.nasa_image_search

import kotlinx.serialization.Serializable

@Serializable
data class NASAImageLibrarySearchDTO(
    val collection: Collection = Collection()
)