package com.sakethh.jetspacer.home.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.home.data.repository.HomeScreenRelatedAPIsRelatedAPIsImplementation
import com.sakethh.jetspacer.home.domain.model.APODDTO
import com.sakethh.jetspacer.home.domain.repository.HomeScreenRelatedAPIsRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchCurrentAPODUseCase (private val homeScreenRelatedAPIsRepository: HomeScreenRelatedAPIsRepository = HomeScreenRelatedAPIsRelatedAPIsImplementation()){
    operator fun invoke(): Flow<NetworkState<APODDTO>> {
        return flow {
            emit(NetworkState.Loading())
            val httpResponse = homeScreenRelatedAPIsRepository.getAPODDataFromTheAPI()
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
                val data = httpResponse.body<APODDTO>()
                emit(NetworkState.Success(data))
            } catch (e: Exception) {
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
}