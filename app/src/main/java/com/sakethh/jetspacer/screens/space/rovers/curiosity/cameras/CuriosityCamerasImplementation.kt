package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras

import androidx.compose.runtime.mutableStateListOf
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.RandomCameraDTO
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@Suppress("LocalVariableName")
class CuriosityCamerasImplementation : CuriosityCamerasService {
    override suspend fun getFHAZData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(roverName = "curiosity", cameraName = "fhaz",sol=sol, page = page).component1()
    }

    override suspend fun getRHAZData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(roverName = "curiosity", cameraName = "rhaz",sol=sol, page = page).component1()
    }

    override suspend fun getMASTData(sol: Int, page: Int): List<Photo>{
        return specificRoverHTTPRequest(roverName = "curiosity", cameraName = "mast",sol=sol, page = page).component1()
    }

    override suspend fun getCHEMCAMData(sol: Int, page: Int):List<Photo> {
        return specificRoverHTTPRequest(roverName = "curiosity", cameraName = "chemcam",sol=sol, page = page).component1()
    }

    override suspend fun getMAHLIData(sol: Int, page: Int): List<Photo>{
        return specificRoverHTTPRequest(roverName = "curiosity", cameraName = "mahli",sol=sol, page = page).component1()
    }

    override suspend fun getMARDIData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(roverName = "curiosity", cameraName = "mardi",sol=sol, page = page).component1()
    }

    override suspend fun getNAVCAMData(sol: Int, page: Int): List<Photo> {
        return specificRoverHTTPRequest(roverName = "curiosity", cameraName = "navcam",sol=sol, page = page).component1()
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
            HTTPClient.ktorClient.get("https://api.nasa.gov/mars-photos/api/v1/rovers/$roverName/photos?sol=$sol&camera=$cameraName&page=$page&api_key=${Constants.NASA_APIKEY}")
                .body<RandomCameraDTO>().photos
        }
        dataList.add(_dataList)
    }
    return dataList.awaitAll()
}