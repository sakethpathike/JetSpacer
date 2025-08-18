package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.ui.screens.explore.ISSLocationState
import com.sakethh.jetspacer.domain.model.iss.ISSLocationDTO
import com.sakethh.jetspacer.domain.repository.ISSInfoRepo
import com.sakethh.jetspacer.ui.utils.extractBodyFlow
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import java.util.Date

class FetchISSLocationUseCase(private val issInfoRepo: ISSInfoRepo) {
    suspend operator fun invoke(): Flow<Response<ISSLocationState>> {
        return extractBodyFlow(httpResponse = issInfoRepo.getISSLocation()) { httpResponse ->
            val originalData = httpResponse.body<ISSLocationDTO>()
            ISSLocationState(
                latitude = originalData.issPosition.latitude,
                longitude = originalData.issPosition.longitude,
                message = originalData.message,
                timestamp = Date(originalData.timestamp.toLong() * 1000).toString(),
                error = false,
                errorMessage = ""
            )
        }
    }
}