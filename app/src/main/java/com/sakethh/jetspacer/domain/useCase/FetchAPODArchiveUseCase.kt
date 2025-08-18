package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.APODDTO
import com.sakethh.jetspacer.domain.repository.NasaRepository
import com.sakethh.jetspacer.ui.utils.extractBodyFlow
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow

class FetchAPODArchiveUseCase(private val nasaRepository: NasaRepository) {
    suspend operator fun invoke(startDate: String, endDate: String): Flow<Response<List<APODDTO>>> =
        extractBodyFlow(
            httpResult = nasaRepository.fetchApodArchive(
                startDate, endDate
            )
        ) { httpResponse ->
            httpResponse.body<List<APODDTO>>().filter {
                it.mediaType == "image"
            }.asReversed()
        }
}