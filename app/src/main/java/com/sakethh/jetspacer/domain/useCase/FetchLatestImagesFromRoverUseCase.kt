package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.data.repository.MarsGalleryImplementation
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.rover_latest_images.RoverLatestImagesDTO
import com.sakethh.jetspacer.domain.repository.MarsGalleryRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchLatestImagesFromRoverUseCase(private val marsGalleryRepository: MarsGalleryRepository = MarsGalleryImplementation()) {
    operator fun invoke(roverName: String): Flow<Response<RoverLatestImagesDTO>> =
        flow {
            emit(Response.Loading())
            val httpResponse = marsGalleryRepository.getLatestImagesFromTheRover(roverName)
            if (httpResponse.status.isSuccess().not()) {
                emit(
                    Response.Failure(
                        exceptionMessage = "Network request failed.",
                        statusCode = httpResponse.status.value,
                        statusDescription = httpResponse.status.description
                    )
                )
                return@flow
            }
            try {
                emit(Response.Success(httpResponse.body()))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    Response.Failure(
                        exceptionMessage = e.message.toString(),
                        statusCode = httpResponse.status.value,
                        statusDescription = httpResponse.status.description
                    )
                )
            }
        }
}