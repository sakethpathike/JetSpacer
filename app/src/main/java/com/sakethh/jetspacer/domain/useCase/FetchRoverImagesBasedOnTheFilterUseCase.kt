package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.CameraAndSolSpecificDTO
import com.sakethh.jetspacer.domain.repository.NasaRepository
import com.sakethh.jetspacer.ui.utils.extractBodyFlow
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow

class FetchRoverImagesBasedOnTheFilterUseCase(private val nasaRepository: NasaRepository) {
    suspend operator fun invoke(
        roverName: String, cameraName: String, sol: Int, page: Int
    ): Flow<Response<CameraAndSolSpecificDTO>> = extractBodyFlow(
        httpResponse = nasaRepository.fetchRoverImages(
            roverName, cameraName, sol, page
        )
    ) { httpResponse ->
        httpResponse.body()
    }
}