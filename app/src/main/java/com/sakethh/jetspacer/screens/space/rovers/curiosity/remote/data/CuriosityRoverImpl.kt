package com.sakethh.jetspacer.screens.space.rovers.curiosity.remote.data

import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.space.rovers.curiosity.remote.data.dto.CuriosityRoverDTO
import io.ktor.client.call.*
import io.ktor.client.request.*

class CuriosityRoverImpl : CuriosityRoverService {
    override suspend fun getRandomImages(sol: Int): CuriosityRoverDTO {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=$sol&page=1&api_key=${Constants.NASA_APIKEY}")
            .body()
    }
}