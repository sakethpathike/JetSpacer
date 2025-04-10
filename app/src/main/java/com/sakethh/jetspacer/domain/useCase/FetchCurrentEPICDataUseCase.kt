package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.data.repository.HomeScreenRelatedAPIsRelatedAPIsImplementation
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.epic.all.AllEPICDTOItem
import com.sakethh.jetspacer.domain.model.epic.specific.EPICSpecificDTO
import com.sakethh.jetspacer.domain.repository.HomeScreenRelatedAPIsRepository
import com.sakethh.jetspacer.ui.screens.home.state.epic.EpicStateItem
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.sqrt

class FetchCurrentEPICDataUseCase (private val homeScreenRelatedAPIsRepository: HomeScreenRelatedAPIsRepository = HomeScreenRelatedAPIsRelatedAPIsImplementation()){
    operator fun invoke(): Flow<Response<List<EpicStateItem>>> {
        return flow {
            try {
                emit(Response.Loading())
                val epicData = mutableListOf<EpicStateItem>()
                try {
                    homeScreenRelatedAPIsRepository.getEpicDataForASpecificDate(
                        homeScreenRelatedAPIsRepository.getAllEpicDataDates()
                            .body<List<AllEPICDTOItem>>().first().date
                    ).body<List<EPICSpecificDTO>>().map {
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
                emit(Response.Success(epicData))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    Response.Failure(
                        exceptionMessage = e.message.toString(),
                        statusCode = 0,
                        statusDescription = e.message.toString()
                    )
                )
            }
        }
    }
}