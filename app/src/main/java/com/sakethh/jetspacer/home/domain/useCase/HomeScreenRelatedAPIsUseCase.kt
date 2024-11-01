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
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.sqrt

class HomeScreenRelatedAPIsUseCase(private val homeScreenRelatedAPIsRepository: HomeScreenRelatedAPIsRepository = HomeScreenRelatedAPIsRelatedAPIsImplementation()) {
    fun apodData(): Flow<NetworkState<APODDTO>> {
        return flow {
            emit(NetworkState.Loading())
            val httpResponse = homeScreenRelatedAPIsRepository.getAPODDataFromTheAPI()
            if (httpResponse.status.isSuccess().not()) {
                emit(
                    NetworkState.Failure(
                        exceptionMessage = "Network request failed.",
                        statusCode = httpResponse.status.value,
                        statusDescription = httpResponse.status.description
                    )
                )
                return@flow
            }
            try {
                val data = httpResponse.body<APODDTO>()
                emit(NetworkState.Success(data))
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

    fun epicData(): Flow<NetworkState<List<EpicStateItem>>> {
        return flow {
            try {
                emit(NetworkState.Loading())
                val epicData = mutableListOf<EpicStateItem>()
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
                    throw e
                    }
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