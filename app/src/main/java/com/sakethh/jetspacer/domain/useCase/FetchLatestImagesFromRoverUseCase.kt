package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.rover_latest_images.RoverLatestImagesDTO
import com.sakethh.jetspacer.domain.repository.NasaRepository
import com.sakethh.jetspacer.ui.utils.extractBodyFlow
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow

class FetchLatestImagesFromRoverUseCase(private val nasaRepository: NasaRepository) {
    suspend operator fun invoke(roverName: String): Flow<Response<RoverLatestImagesDTO>> =
        extractBodyFlow(httpResult = nasaRepository.fetchLatestRoverImages(roverName)) { httpResponse ->
            httpResponse.body()
        }
}