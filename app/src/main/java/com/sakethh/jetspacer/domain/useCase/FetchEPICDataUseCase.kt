package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.core.common.utils.logger
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.epic.specific.EPICSpecificDTO
import com.sakethh.jetspacer.domain.onFailure
import com.sakethh.jetspacer.domain.onSuccess
import com.sakethh.jetspacer.domain.repository.NasaRepository
import com.sakethh.jetspacer.ui.screens.home.state.epic.EpicStateItem
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

class FetchEPICDataUseCase(private val nasaRepository: NasaRepository) {
    operator fun invoke(): Flow<Response<List<EpicStateItem>>> {
        return flow<Response<List<EpicStateItem>>> {
            emit(Response.Loading())
            nasaRepository.fetchEpicAvailableDates().onSuccess { httpResponse ->
                mutableListOf<EpicStateItem>().apply {
                    httpResponse.body<List<EPICSpecificDTO>>().map {
                        EpicStateItem(
                            imageURL = "https://epic.gsfc.nasa.gov/archive/natural/${
                                it.date.substringBefore(" ").replace("-", "/")
                            }/png/${it.image}.png",
                            timeWhenImageWasCaptured = it.date.substringAfter(" "),
                            distanceToEarthFromTheEPIC = sqrt(
                                (it.positionOfTheSatelliteInSpace.x * it.positionOfTheSatelliteInSpace.x) + (it.positionOfTheSatelliteInSpace.y * it.positionOfTheSatelliteInSpace.y) + (it.positionOfTheSatelliteInSpace.z * it.positionOfTheSatelliteInSpace.z)
                            ).roundToLong(),
                            distanceBetweenSunAndEarth = sqrt(
                                (it.positionOfTheSunInSpace.x * it.positionOfTheSunInSpace.x) + (it.positionOfTheSunInSpace.y * it.positionOfTheSunInSpace.y) + (it.positionOfTheSunInSpace.z * it.positionOfTheSunInSpace.z)
                            ).roundToLong(),
                            date = it.date.substringBefore(" "),
                            distanceBetweenEarthAndMoon = sqrt(
                                (it.positionOfTheMoonInSpace.x * it.positionOfTheMoonInSpace.x) + (it.positionOfTheMoonInSpace.y * it.positionOfTheMoonInSpace.y) + (it.positionOfTheMoonInSpace.z * it.positionOfTheMoonInSpace.z)
                            ).roundToLong(),
                            distanceBetweenSunAndEpic = sqrt(
                                (it.positionOfTheSunInSpace.x - it.positionOfTheSatelliteInSpace.x).pow(
                                    2
                                ) + (it.positionOfTheSunInSpace.y - it.positionOfTheSatelliteInSpace.y).pow(
                                    2
                                ) + (it.positionOfTheSunInSpace.z - it.positionOfTheSatelliteInSpace.z).pow(
                                    2
                                )
                            ).roundToLong()
                        )
                    }.forEach {
                        add(it)
                    }
                }.apply {
                    emit(Response.Success(this.toList()))
                }
            }.onFailure {
                emit(
                    Response.Failure(
                        exceptionMessage = it.exceptionMessage,
                        statusCode = it.statusCode,
                        statusDescription = it.statusDescription
                    )
                )
            }
        }.catch {
            when(it){
                is NoTransformationFoundException -> emit(
                    Response.Failure(
                        exceptionMessage = it.cause?.message ?: "",
                        statusCode = 0,
                        statusDescription = "It seems NASA's server didn’t respond with the usual format; the service might be down for now.",
                    )
                )
                else -> emit(
                    Response.Failure(
                        exceptionMessage = it.cause?.message ?: "",
                        statusCode = 0,
                        statusDescription = it.message ?: "",
                    )
                )
            }
        }
    }
}