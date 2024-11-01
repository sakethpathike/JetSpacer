package com.sakethh.jetspacer.explore.apodArchive.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.explore.apodArchive.data.repository.APODArchiveImplementation
import com.sakethh.jetspacer.explore.apodArchive.domain.repository.APODArchiveRepository
import com.sakethh.jetspacer.home.domain.model.APODDTO
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class APODArchiveUseCase(private val apodArchiveRepository: APODArchiveRepository = APODArchiveImplementation()) {
    operator fun invoke(startDate: String, endDate: String): Flow<NetworkState<List<APODDTO>>> =
        flow {
            val httpResponse = apodArchiveRepository.getAPODArchiveData(startDate, endDate)
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
                emit(NetworkState.Loading(""))
                val apodArchiveData =
                    httpResponse.body<List<APODDTO>>().filter {
                        it.mediaType == "image"
                    }.asReversed()
                emit(NetworkState.Success(apodArchiveData))
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