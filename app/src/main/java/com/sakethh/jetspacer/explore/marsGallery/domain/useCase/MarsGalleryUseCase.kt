package com.sakethh.jetspacer.explore.marsGallery.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.explore.marsGallery.data.repository.MarsGalleryImplementation
import com.sakethh.jetspacer.explore.marsGallery.domain.model.CameraAndSolSpecificDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.RoverLatestImagesDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.repository.MarsGalleryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MarsGalleryUseCase(private val marsGalleryRepository: MarsGalleryRepository = MarsGalleryImplementation()) {

    fun getLatestImagesFromTheRover(roverName: String): Flow<NetworkState<RoverLatestImagesDTO>> =
        flow {
        try {
            emit(NetworkState.Loading())
            emit(NetworkState.Success(marsGalleryRepository.getLatestImagesFromTheRover(roverName)))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkState.Failure(e.message.toString()))
        }
    }

    fun getImagesBasedOnTheFilter(
        roverName: String,
        cameraName: String,
        sol: Int,
        page: Int
    ): Flow<NetworkState<CameraAndSolSpecificDTO>> = flow {
        try {
            emit(NetworkState.Loading())
            emit(
                NetworkState.Success(
                    marsGalleryRepository.getImagesBasedOnTheFilter(
                        roverName,
                        cameraName,
                        sol,
                        page
                    )
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkState.Failure(e.message.toString()))
        }
    }
}