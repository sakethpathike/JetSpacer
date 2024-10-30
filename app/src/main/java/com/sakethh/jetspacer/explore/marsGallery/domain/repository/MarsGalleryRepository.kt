package com.sakethh.jetspacer.explore.marsGallery.domain.repository

import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.RoverLatestImagesDTO

interface MarsGalleryRepository {
    suspend fun getLatestImagesFromTheRover(roverName: String): RoverLatestImagesDTO
}