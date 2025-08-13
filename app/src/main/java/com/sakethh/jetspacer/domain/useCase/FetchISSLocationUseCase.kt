package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.data.repository.ExploreScreenRelatedAPIsImplementation
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.iss.modified.ISSLocationModifiedDTO
import com.sakethh.jetspacer.domain.model.iss.source.ISSLocationDTO
import com.sakethh.jetspacer.domain.repository.ExploreScreenRelatedAPIsRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import java.util.Date

class FetchISSLocationUseCase(private val exploreScreenRelatedAPIsRepository: ExploreScreenRelatedAPIsRepository = ExploreScreenRelatedAPIsImplementation()) {
    suspend operator fun invoke(): Response<ISSLocationModifiedDTO> {
        val httpResponse = exploreScreenRelatedAPIsRepository.getISSLocation()
        if (httpResponse.status.isSuccess().not()) {
            return Response.Failure(
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
                timestamp = Date(originalData.timestamp.toLong() * 1000).toString(),
                error = false,
                errorMessage = ""
            )
            Response.Success(modifiedData)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(
                exceptionMessage = e.message.toString(),
                statusCode = httpResponse.status.value,
                statusDescription = httpResponse.status.description
            )
        }
    }
}