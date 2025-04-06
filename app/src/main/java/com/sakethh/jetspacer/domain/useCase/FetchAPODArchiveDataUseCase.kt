package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.data.repository.APODArchiveImplementation
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.APODDTO
import com.sakethh.jetspacer.domain.repository.APODArchiveRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchAPODArchiveDataUseCase(private val apodArchiveRepository: APODArchiveRepository = APODArchiveImplementation()) {
    operator fun invoke(startDate: String, endDate: String): Flow<Response<List<APODDTO>>> =
        flow {
            emit(Response.Loading())
            val httpResponse = apodArchiveRepository.getAPODArchiveData(startDate, endDate)
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
                val apodArchiveData =
                    httpResponse.body<List<APODDTO>>().filter {
                        it.mediaType == "image"
                    }.asReversed()
                emit(Response.Success(apodArchiveData))
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