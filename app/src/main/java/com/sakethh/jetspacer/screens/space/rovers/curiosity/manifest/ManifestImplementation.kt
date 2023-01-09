package com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest

import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.dto.RoverManifestDTO
import io.ktor.client.call.*
import io.ktor.client.request.*

class ManifestImplementation : ManifestService {
    override suspend fun getCuriosityMaxSol(): Int {
        return manifestDataRequest<RoverManifestDTO>(roverName = RoverNameForManifestData.CURIOSITY).photo_manifest.max_sol
    }
}

suspend inline fun <reified T : Any> manifestDataRequest(roverName: String): T {
    return HTTPClient.ktorClient.get("https://api.nasa.gov/mars-photos/api/v1/manifests/$roverName?api_key=${Constants.NASA_APIKEY}")
        .body()
}

object RoverNameForManifestData {
    const val CURIOSITY = "curiosity"
    const val SPIRIT = "spirit"
    const val OPPORTUNITY = "opportunity"
}