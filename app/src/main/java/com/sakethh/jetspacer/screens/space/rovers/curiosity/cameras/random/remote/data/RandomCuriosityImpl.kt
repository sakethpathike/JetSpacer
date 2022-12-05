package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data

import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.RandomCameraDTO
import io.ktor.client.call.*
import io.ktor.client.request.*

class RandomCameraCuriosityImpl : RandomCuriosityService {
    override suspend fun getRandomCuriosityData(sol: Int, page: Int): RandomCameraDTO {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=$sol&page=$page&api_key=${Constants.NASA_APIKEY}")
            .body()
    }
}