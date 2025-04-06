package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.data.repository.MarsGalleryImplementation
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.CameraAndSolSpecificDTO
import com.sakethh.jetspacer.domain.repository.MarsGalleryRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchImagesBasedOnTheFilterUseCase(private val marsGalleryRepository: MarsGalleryRepository = MarsGalleryImplementation()) {
    operator fun invoke(
        roverName: String,
        cameraName: String,
        sol: Int,
        page: Int
    ): Flow<Response<CameraAndSolSpecificDTO>> = flow {
        emit(Response.Loading())
        val httpResponse = marsGalleryRepository.getImagesBasedOnTheFilter(
            roverName,
            cameraName,
            sol,
            page
        )
        if (httpResponse.status.isSuccess().not()) {
            emit(
                Response.Failure(
                    exceptionMessage = "",
                    statusCode = httpResponse.status.value,
                    statusDescription = httpResponse.status.description
                )
            )
            return@flow
        }
        try {
            emit(
                Response.Success(
                    httpResponse.body()
                )
            )
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