package com.sakethh.jetspacer.home.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.home.data.repository.APODAPIImplementation
import com.sakethh.jetspacer.home.domain.model.APODDTO
import com.sakethh.jetspacer.home.domain.repository.APODAPIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class APODApiUseCase(private val apodAPIRepository: APODAPIRepository = APODAPIImplementation()) {
    operator fun invoke(): Flow<NetworkState<APODDTO>> {
        return flow {
            try {
                emit(NetworkState.Loading(""))
                emit(NetworkState.Success(apodAPIRepository.getAPODDataFromTheAPI()))
            } catch (e: Exception) {
                emit(NetworkState.Failure(e.message.toString()))
            }
        }
    }
}