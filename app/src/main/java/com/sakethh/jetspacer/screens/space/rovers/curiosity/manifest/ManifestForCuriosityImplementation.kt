package com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest

import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Camera
import com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.dto.RoverManifestDTO
import io.ktor.client.call.*
import io.ktor.client.request.*

class ManifestForCuriosityImplementation : ManifestService {
    override suspend fun getCuriosityMaxSol(): Int {
        return try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            manifestForCuriosityDataRequest<RoverManifestDTO>(roverName = RoverNameForManifestData.CURIOSITY).photo_manifest.max_sol
        }catch (_:Exception){
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            0
        }
    }
}

suspend inline fun <reified T : Any> manifestForCuriosityDataRequest(roverName: String): T {
    return try {
        HomeScreenViewModel.Network.isConnectionSucceed.value = true
        HTTPClient.ktorClientWithCache.get("https://api.nasa.gov/mars-photos/api/v1/manifests/$roverName?api_key=${BookMarksVM.dbImplementation.localDBData().getAPIKeys()[0].currentNASAAPIKey}")
            .body()
    }catch (_:Exception){
        HomeScreenViewModel.Network.isConnectionSucceed.value = false
        RoverManifestDTO as T
    }
}

object RoverNameForManifestData {
    const val CURIOSITY = "curiosity"
    const val SPIRIT = "spirit"
    const val OPPORTUNITY = "opportunity"
}