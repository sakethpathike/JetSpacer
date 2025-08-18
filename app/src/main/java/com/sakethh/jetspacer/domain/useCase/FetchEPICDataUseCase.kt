package com.sakethh.jetspacer.domain.useCase

import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.epic.AllEPICDTOItem
import com.sakethh.jetspacer.domain.model.epic.specific.EPICSpecificDTO
import com.sakethh.jetspacer.domain.repository.NasaRepository
import com.sakethh.jetspacer.ui.screens.home.state.epic.EpicStateItem
import com.sakethh.jetspacer.ui.utils.extractBodyFlow
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

class FetchEPICDataUseCase(private val nasaRepository: NasaRepository) {
    suspend operator fun invoke(): Flow<Response<List<EpicStateItem>>> {
        val date: String? = when (val availableDates = nasaRepository.fetchEpicAvailableDates()) {
            is Response.Success -> availableDates.data.body<List<AllEPICDTOItem>>().first().date
            else -> null
        }
        if (date == null) {
            return flow {
                emit(Response.Failure.clientFailure())
            }
        }
        return extractBodyFlow(
            httpResult = nasaRepository.fetchEpicImages(
                date = date
            )
        ) { httpResponse ->
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
            }
        }
    }
}