package com.sakethh.jetspacer.explore.marsGallery.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.common.utils.Constants
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.RoverLatestImagesDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.repository.MarsGalleryRepository
import io.ktor.client.call.body
import io.ktor.client.request.get

class MarsGalleryImplementation : MarsGalleryRepository {
    override suspend fun getLatestImagesFromTheRover(roverName: String): RoverLatestImagesDTO {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/mars-photos/api/v1/rovers/$roverName/latest_photos?api_key=${Constants.NASA_API_KEY}")
            .body()
    }
}