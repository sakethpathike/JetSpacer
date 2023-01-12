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
        return com.sakethh.jetspacer.screens.space.rovers.opportunity.specificRoverHTTPRequest(
            roverName = "curiosity",
            cameraName = "fhaz",
            sol = sol,
            page = page
        ).component1()
    }

    override suspend fun getRHAZData(sol: Int, page: Int): List<Photo> {
        return com.sakethh.jetspacer.screens.space.rovers.opportunity.specificRoverHTTPRequest(
            roverName = "curiosity",
            cameraName = "rhaz",
            sol = sol,
            page = page
        ).component1()
    }

    override suspend fun getMASTData(sol: Int, page: Int): List<Photo> {
        return com.sakethh.jetspacer.screens.space.rovers.opportunity.specificRoverHTTPRequest(
            roverName = "curiosity",
            cameraName = "mast",
            sol = sol,
            page = page
        ).component1()
    }

    override suspend fun getCHEMCAMData(sol: Int, page: Int): List<Photo> {
        return com.sakethh.jetspacer.screens.space.rovers.opportunity.specificRoverHTTPRequest(
            roverName = "curiosity",
            cameraName = "chemcam",
            sol = sol,
            page = page
        ).component1()
    }

    override suspend fun getMAHLIData(sol: Int, page: Int): List<Photo> {
        return com.sakethh.jetspacer.screens.space.rovers.opportunity.specificRoverHTTPRequest(
            roverName = "curiosity",
            cameraName = "mahli",
            sol = sol,
            page = page
        ).component1()
    }

    override suspend fun getMARDIData(sol: Int, page: Int): List<Photo> {
        return com.sakethh.jetspacer.screens.space.rovers.opportunity.specificRoverHTTPRequest(
            roverName = "curiosity",
            cameraName = "mardi",
            sol = sol,
            page = page
        ).component1()
    }

    override suspend fun getNAVCAMData(sol: Int, page: Int): List<Photo> {
        return com.sakethh.jetspacer.screens.space.rovers.opportunity.specificRoverHTTPRequest(
            roverName = "curiosity",
            cameraName = "navcam",
            sol = sol,
            page = page
        ).component1()
    }
}