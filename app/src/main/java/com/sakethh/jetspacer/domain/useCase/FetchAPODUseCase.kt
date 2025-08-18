package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.APODDTO
import com.sakethh.jetspacer.domain.repository.NasaRepository
import com.sakethh.jetspacer.ui.utils.extractBodyFlow
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow

class FetchAPODUseCase (private val nasaRepository: NasaRepository){
    suspend operator fun invoke(): Flow<Response<APODDTO>> {
        return extractBodyFlow(httpResult = nasaRepository.fetchApod()) { httpResponse ->
            httpResponse.body()
        }
    }
}