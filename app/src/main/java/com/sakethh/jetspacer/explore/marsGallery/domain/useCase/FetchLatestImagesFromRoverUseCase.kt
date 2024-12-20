package com.sakethh.jetspacer.explore.marsGallery.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.explore.marsGallery.data.repository.MarsGalleryImplementation
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.RoverLatestImagesDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.repository.MarsGalleryRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchLatestImagesFromRoverUseCase(private val marsGalleryRepository: MarsGalleryRepository = MarsGalleryImplementation()) {
    operator fun invoke(roverName: String): Flow<NetworkState<RoverLatestImagesDTO>> =
        flow {
            emit(NetworkState.Loading())
            val httpResponse = marsGalleryRepository.getLatestImagesFromTheRover(roverName)
            if (httpResponse.status.isSuccess().not()) {
                emit(
                    NetworkState.Failure(
                        exceptionMessage = "Network request failed.",
                        statusCode = httpResponse.status.value,
                        statusDescription = httpResponse.status.description
                    )
                )
                return@flow
            }
            try {
                emit(NetworkState.Success(httpResponse.body()))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    NetworkState.Failure(
                        exceptionMessage = e.message.toString(),
                        statusCode = httpResponse.status.value,
                        statusDescription = httpResponse.status.description
                    )
                )
            }
        }
}