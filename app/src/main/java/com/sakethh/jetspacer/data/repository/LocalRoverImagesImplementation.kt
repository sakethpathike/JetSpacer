package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.JetSpacerApplication
import com.sakethh.jetspacer.domain.model.rover.RoverImage
import com.sakethh.jetspacer.domain.repository.LocalRoverImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class LocalRoverImagesImplementation : LocalRoverImagesRepository {
    override suspend fun addANewImage(roverImage: RoverImage) {
        JetSpacerApplication.getLocalDb()?.roverImagesDao?.addANewImage(roverImage)
    }

    override suspend fun deleteAnImage(url: String) {
        JetSpacerApplication.getLocalDb()?.roverImagesDao?.deleteAnImage(url)
    }

    override suspend fun doesThisImageExists(url: String): Boolean {
        return JetSpacerApplication.getLocalDb()?.roverImagesDao?.doesThisImageExists(url) ?: false
    }

    override fun getAllBookmarkedImages(): Flow<List<RoverImage>> {
        return JetSpacerApplication.getLocalDb()?.roverImagesDao?.getAllBookmarkedImages()
            ?: emptyFlow()
    }
}