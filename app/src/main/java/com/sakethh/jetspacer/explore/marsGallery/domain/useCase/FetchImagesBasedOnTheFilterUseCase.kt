package com.sakethh.jetspacer.explore.marsGallery.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.explore.marsGallery.data.repository.MarsGalleryImplementation
import com.sakethh.jetspacer.explore.marsGallery.domain.model.CameraAndSolSpecificDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.repository.MarsGalleryRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchImagesBasedOnTheFilterUseCase(private val marsGalleryRepository: MarsGalleryRepository = MarsGalleryImplementation()) {
    operator fun invoke(
        roverName: String,
        cameraName: String,
        sol: Int,
        page: Int
    ): Flow<NetworkState<CameraAndSolSpecificDTO>> = flow {
        emit(NetworkState.Loading())
        val httpResponse = marsGalleryRepository.getImagesBasedOnTheFilter(
            roverName,
            cameraName,
            sol,
            page
        )
        if (httpResponse.status.isSuccess().not()) {
            emit(
                NetworkState.Failure(
                    exceptionMessage = "",
                    statusCode = httpResponse.status.value,
                    statusDescription = httpResponse.status.description
                )
            )
            return@flow
        }
        try {
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