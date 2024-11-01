package com.sakethh.jetspacer.explore.marsGallery.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.explore.marsGallery.domain.repository.MarsGalleryRepository
import com.sakethh.jetspacer.home.settings.presentation.utils.GlobalSettings
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class MarsGalleryImplementation : MarsGalleryRepository {
    override suspend fun getLatestImagesFromTheRover(roverName: String): HttpResponse {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/mars-photos/api/v1/rovers/$roverName/latest_photos?api_key=${GlobalSettings.nasaAPIKey.value}")
    }

    override suspend fun getImagesBasedOnTheFilter(
        roverName: String,
        cameraName: String,
        sol: Int,
        page: Int,
    ): HttpResponse {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/mars-photos/api/v1/rovers/$roverName/photos?sol=$sol&camera=$cameraName&page=$page&api_key=${GlobalSettings.nasaAPIKey.value}")
    }
}