package com.sakethh.jetspacer.explore.marsGallery.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.explore.marsGallery.data.repository.MarsGalleryImplementation
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.RoverLatestImagesDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.repository.MarsGalleryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MarsGalleryUseCase(private val marsGalleryRepository: MarsGalleryRepository = MarsGalleryImplementation()) {
    operator fun invoke(roverName: String): Flow<NetworkState<RoverLatestImagesDTO>> = flow {
        try {
            emit(NetworkState.Loading())
            emit(NetworkState.Success(marsGalleryRepository.getLatestImagesFromTheRover(roverName)))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkState.Failure(e.message.toString()))
        }
    }
}