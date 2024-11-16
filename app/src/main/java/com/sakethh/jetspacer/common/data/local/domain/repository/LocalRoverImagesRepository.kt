package com.sakethh.jetspacer.common.data.local.domain.repository

import com.sakethh.jetspacer.common.data.local.domain.model.rover.RoverImage
import kotlinx.coroutines.flow.Flow

interface LocalRoverImagesRepository {
    suspend fun addANewImage(roverImage: RoverImage)

    suspend fun deleteAnImage(url: String)

    suspend fun doesThisImageExists(url: String): Boolean

    fun getAllBookmarkedImages(): Flow<List<RoverImage>>
}