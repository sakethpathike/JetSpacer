package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.common.Network
import com.sakethh.jetspacer.domain.repository.MarsGalleryRepository
import com.sakethh.jetspacer.ui.GlobalSettings
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class MarsGalleryImplementation : MarsGalleryRepository {
    override suspend fun getLatestImagesFromTheRover(roverName: String): HttpResponse {
        return Network.ktorClient.get("https://api.nasa.gov/mars-photos/api/v1/rovers/$roverName/latest_photos?api_key=${GlobalSettings.nasaAPIKey.value}")
    }

    override suspend fun getImagesBasedOnTheFilter(
        roverName: String,
        cameraName: String,
        sol: Int,
        page: Int,
    ): HttpResponse {
        return Network.ktorClient.get("https://api.nasa.gov/mars-photos/api/v1/rovers/$roverName/photos?sol=$sol&camera=$cameraName&page=$page&api_key=${GlobalSettings.nasaAPIKey.value}")
    }
}