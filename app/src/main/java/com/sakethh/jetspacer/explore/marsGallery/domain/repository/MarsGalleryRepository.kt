package com.sakethh.jetspacer.explore.marsGallery.domain.repository

import com.sakethh.jetspacer.explore.marsGallery.domain.model.CameraAndSolSpecificDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.RoverLatestImagesDTO


interface MarsGalleryRepository {
    suspend fun getLatestImagesFromTheRover(roverName: String): RoverLatestImagesDTO

    suspend fun getImagesBasedOnTheFilter(
        roverName: String,
        cameraName: String,
        sol: Int,
        page: Int
    ): CameraAndSolSpecificDTO
}