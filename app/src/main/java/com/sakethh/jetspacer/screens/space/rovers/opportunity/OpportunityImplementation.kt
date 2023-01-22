package com.sakethh.jetspacer.screens.space.rovers.opportunity

import androidx.compose.runtime.mutableStateListOf
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.RandomCameraDTO
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@Suppress("LocalVariableName")
class OpportunityCamerasImplementation : OpportunityCamerasService {
    override suspend fun getRandomCamerasData(sol: Int, page: Int): RandomCameraDTO {
        return try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            HTTPClient.ktorClientWithCache.get("https://api.nasa.gov/mars-photos/api/v1/rovers/opportunity/photos?sol=$sol&page=$page&api_key=${Constants.NASA_APIKEY}")
                .body()
        } catch (_: Exception) {
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            RandomCameraDTO(emptyList())
        }
    }

    override suspend fun getFHAZData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(
            roverName = "opportunity",
            cameraName = "fhaz",
            sol = sol,
            page = page
        ).component1()
    }


    override suspend fun getRHAZData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(
            roverName = "opportunity",
            cameraName = "rhaz",
            sol = sol,
            page = page
        ).component1()
    }


    override suspend fun getNAVCAMData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(
            roverName = "opportunity",
            cameraName = "navcam",
            sol = sol,
            page = page
        ).component1()
    }

    override suspend fun getPANCAMData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(
            roverName = "opportunity",
            cameraName = "pancam",
            sol = sol,
            page = page
        ).component1()
    }

    override suspend fun getMINITESData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(
            roverName = "opportunity",
            cameraName = "minites",
            sol = sol,
            page = page
        ).component1()
    }
}

suspend fun specificRoverHTTPRequest(
    roverName: String,
    cameraName: String,
    sol: Int,
    page: Int
): List<List<Photo>> {
    val dataList = mutableStateListOf<Deferred<List<Photo>>>()
    coroutineScope {
        val _dataList = async {
            try {
                HomeScreenViewModel.Network.isConnectionSucceed.value = true
                HTTPClient.ktorClientWithCache.get("https://api.nasa.gov/mars-photos/api/v1/rovers/$roverName/photos?sol=$sol&camera=$cameraName&page=$page&api_key=${Constants.NASA_APIKEY}")
                    .body<RandomCameraDTO>().photos
            } catch (_: Exception) {
                HomeScreenViewModel.Network.isConnectionSucceed.value = false
                emptyList()
            }
        }
        dataList.add(_dataList)
    }
    return dataList.awaitAll()
}