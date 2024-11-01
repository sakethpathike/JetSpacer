package com.sakethh.jetspacer.home.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.home.data.repository.HomeScreenRelatedAPIsRelatedAPIsImplementation
import com.sakethh.jetspacer.home.domain.model.APODDTO
import com.sakethh.jetspacer.home.domain.model.epic.all.AllEPICDTOItem
import com.sakethh.jetspacer.home.domain.model.epic.specific.EPICSpecificDTO
import com.sakethh.jetspacer.home.domain.repository.HomeScreenRelatedAPIsRepository
import com.sakethh.jetspacer.home.presentation.state.epic.EpicStateItem
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class HomeScreenRelatedAPIsUseCase(private val homeScreenRelatedAPIsRepository: HomeScreenRelatedAPIsRepository = HomeScreenRelatedAPIsRelatedAPIsImplementation()) {
    fun apodData(): Flow<NetworkState<APODDTO>> {
        return flow {
            val httpResponse = homeScreenRelatedAPIsRepository.getAPODDataFromTheAPI()
            try {
                emit(NetworkState.Loading(""))
                emit(NetworkState.Success(httpResponse.body()))
            } catch (e: Exception) {
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

    fun epicData(coroutineScope: CoroutineScope): Flow<NetworkState<List<EpicStateItem>>> {
        return flow {
            try {
                emit(NetworkState.Loading(""))
                val epicData = mutableListOf<EpicStateItem>()
                val epicDataJob = coroutineScope.launch {
                    try {
                        homeScreenRelatedAPIsRepository.getEpicDataForASpecificDate(
                            homeScreenRelatedAPIsRepository.getAllEpicDataDates()
                                .body<List<AllEPICDTOItem>>().first().date
                        ).body<List<EPICSpecificDTO>>().map {
                            jetSpacerLog(
                                sqrt(
                                    (it.positionOfTheSunInSpace.x * it.positionOfTheSunInSpace.x)
                                            + (it.positionOfTheSunInSpace.y * it.positionOfTheSunInSpace.y)
                                            + (it.positionOfTheSunInSpace.z * it.positionOfTheSunInSpace.z)
                                ).toLong().toString()
                            )
                            EpicStateItem(
                                imageURL = "https://epic.gsfc.nasa.gov/archive/natural/${
                                    it.date.substringBefore(" ").replace("-", "/")
                                }/png/${it.image}.png",
                                timeWhenImageWasCaptured = it.date.substringAfter(" "),
                                distanceToEarthFromTheEPIC = sqrt(
                                    (it.positionOfTheSatelliteInSpace.x * it.positionOfTheSatelliteInSpace.x)
                                            + (it.positionOfTheSatelliteInSpace.y * it.positionOfTheSatelliteInSpace.y)
                                            + (it.positionOfTheSatelliteInSpace.z * it.positionOfTheSatelliteInSpace.z)
                                ).toLong(),
                                distanceToSunFromEPIC = sqrt(
                                    (it.positionOfTheSunInSpace.x * it.positionOfTheSunInSpace.x)
                                            + (it.positionOfTheSunInSpace.y * it.positionOfTheSunInSpace.y)
                                            + (it.positionOfTheSunInSpace.z * it.positionOfTheSunInSpace.z)
                                ).toLong(),
                                date = it.date.substringBefore(" "),
                                distanceBetweenEarthToMoon = 0
                            )
                        }.forEach {
                            epicData.add(it)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        emit(
                            NetworkState.Failure(
                                exceptionMessage = e.message.toString(),
                                statusCode = 0,
                                statusDescription = e.message.toString()
                            )
                        )
                    }
                }
                epicDataJob.join()
                emit(NetworkState.Success(epicData))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    NetworkState.Failure(
                        exceptionMessage = e.message.toString(),
                        statusCode = 0,
                        statusDescription = e.message.toString()
                    )
                )
            }
        }
    }
}