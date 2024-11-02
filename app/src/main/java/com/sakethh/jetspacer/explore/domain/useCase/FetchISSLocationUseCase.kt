package com.sakethh.jetspacer.explore.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.explore.data.repository.ExploreScreenRelatedAPIsImplementation
import com.sakethh.jetspacer.explore.domain.model.api.iss.modified.ISSLocationModifiedDTO
import com.sakethh.jetspacer.explore.domain.model.api.iss.source.ISSLocationDTO
import com.sakethh.jetspacer.explore.domain.repository.ExploreScreenRelatedAPIsRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import java.util.Date

class FetchISSLocationUseCase(private val exploreScreenRelatedAPIsRepository: ExploreScreenRelatedAPIsRepository = ExploreScreenRelatedAPIsImplementation()) {
    suspend operator fun invoke(): NetworkState<ISSLocationModifiedDTO> {
        val httpResponse = exploreScreenRelatedAPIsRepository.getISSLocation()
        if (httpResponse.status.isSuccess().not()) {
            return NetworkState.Failure(
                exceptionMessage = "",
                statusCode = httpResponse.status.value,
                statusDescription = httpResponse.status.description
            )
        }
        return try {
            val originalData = httpResponse.body<ISSLocationDTO>()
            val modifiedData = ISSLocationModifiedDTO(
                latitude = originalData.issPosition.latitude,
                longitude = originalData.issPosition.longitude,
                message = originalData.message,
                timestamp = Date(originalData.timestamp.toLong() * 1000).toString()
            )
            NetworkState.Success(modifiedData)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkState.Failure(
                exceptionMessage = e.message.toString(),
                statusCode = httpResponse.status.value,
                statusDescription = httpResponse.status.description
            )
        }
    }
}