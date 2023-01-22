package com.sakethh.jetspacer.screens.space.rovers.spirit

import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.RandomCameraDTO
import com.sakethh.jetspacer.screens.space.rovers.opportunity.specificRoverHTTPRequest
import io.ktor.client.call.*
import io.ktor.client.request.*

@Suppress("LocalVariableName")
class SpiritCamerasImplementation : SpiritCamerasService {
    override suspend fun getRandomCamerasData(sol: Int, page: Int): RandomCameraDTO {
        return try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            HTTPClient.ktorClientWithCache.get("https://api.nasa.gov/mars-photos/api/v1/rovers/spirit/photos?sol=$sol&page=$page&api_key=${Constants.NASA_APIKEY}")
                .body()
        } catch (_: Exception) {
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            RandomCameraDTO(emptyList())
        }
    }

    override suspend fun getFHAZData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(
            roverName = "spirit",
            cameraName = "fhaz",
            sol = sol,
            page = page
        ).component1()
    }

    override suspend fun getRHAZData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(
            roverName = "spirit",
            cameraName = "rhaz",
            sol = sol,
            page = page
        ).component1()
    }


    override suspend fun getNAVCAMData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(
            roverName = "spirit",
            cameraName = "navcam",
            sol = sol,
            page = page
        ).component1()
    }

    override suspend fun getPANCAMData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(
            roverName = "spirit",
            cameraName = "pancam",
            sol = sol,
            page = page
        ).component1()
    }

    override suspend fun getMINITESData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(
            roverName = "spirit",
            cameraName = "minites",
            sol = sol,
            page = page
        ).component1()
    }
}