package com.sakethh.jetspacer.explore.domain.model.api.nasa

import kotlinx.serialization.Serializable

@Serializable
data class NASAImageLibrarySearchDTO(
    val collection: Collection = Collection()
)