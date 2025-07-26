package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.HyleApplication
import com.sakethh.jetspacer.domain.model.rover.RoverImage
import com.sakethh.jetspacer.domain.repository.LocalRoverImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class LocalRoverImagesImplementation : LocalRoverImagesRepository {
    override suspend fun addANewImage(roverImage: RoverImage) {
        HyleApplication.getLocalDb()?.roverImagesDao?.addANewImage(roverImage)
    }

    override suspend fun deleteAnImage(url: String) {
        HyleApplication.getLocalDb()?.roverImagesDao?.deleteAnImage(url)
    }

    override suspend fun doesThisImageExists(url: String): Boolean {
        return HyleApplication.getLocalDb()?.roverImagesDao?.doesThisImageExists(url) ?: false
    }

    override fun getAllBookmarkedImages(): Flow<List<RoverImage>> {
        return HyleApplication.getLocalDb()?.roverImagesDao?.getAllBookmarkedImages()
            ?: emptyFlow()
    }
}