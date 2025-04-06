package com.sakethh.jetspacer.domain.repository

import io.ktor.client.statement.HttpResponse

interface MarsGalleryRepository {
    suspend fun getLatestImagesFromTheRover(roverName: String): HttpResponse

    suspend fun getImagesBasedOnTheFilter(
        roverName: String,
        cameraName: String,
        sol: Int,
        page: Int
    ): HttpResponse
}