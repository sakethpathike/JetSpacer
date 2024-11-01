package com.sakethh.jetspacer.explore.marsGallery.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.explore.marsGallery.data.repository.MarsGalleryImplementation
import com.sakethh.jetspacer.explore.marsGallery.domain.model.CameraAndSolSpecificDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.RoverLatestImagesDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.repository.MarsGalleryRepository
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MarsGalleryUseCase(private val marsGalleryRepository: MarsGalleryRepository = MarsGalleryImplementation()) {

    fun getLatestImagesFromTheRover(roverName: String): Flow<NetworkState<RoverLatestImagesDTO>> =
        flow {
            val httpResponse = marsGalleryRepository.getLatestImagesFromTheRover(roverName)
        try {
            emit(NetworkState.Loading())
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

    fun getImagesBasedOnTheFilter(
        roverName: String,
        cameraName: String,
        sol: Int,
        page: Int
    ): Flow<NetworkState<CameraAndSolSpecificDTO>> = flow {
        val httpResponse = marsGalleryRepository.getImagesBasedOnTheFilter(
            roverName,
            cameraName,
            sol,
            page
        )
        try {
            emit(NetworkState.Loading())
            emit(
                NetworkState.Success(
                    httpResponse.body()
                )
            )
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