package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.data.repository.HomeScreenRelatedAPIsRelatedAPIsImplementation
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.APODDTO
import com.sakethh.jetspacer.domain.repository.HomeScreenRelatedAPIsRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchCurrentAPODUseCase (private val homeScreenRelatedAPIsRepository: HomeScreenRelatedAPIsRepository = HomeScreenRelatedAPIsRelatedAPIsImplementation()){
    operator fun invoke(): Flow<Response<APODDTO>> {
        return flow {
            emit(Response.Loading())
            val httpResponse = homeScreenRelatedAPIsRepository.getAPODDataFromTheAPI()
            if (!httpResponse.status.isSuccess()) {
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
                val data = httpResponse.body<APODDTO>()
                emit(Response.Success(data))
            } catch (e: Exception) {
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
}