package com.sakethh.jetspacer.home.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.home.data.repository.HomeScreenRelatedAPIsRelatedAPIsImplementation
import com.sakethh.jetspacer.home.domain.model.APODDTO
import com.sakethh.jetspacer.home.domain.model.epic.specific.EPICSpecificDTO
import com.sakethh.jetspacer.home.domain.repository.HomeScreenRelatedAPIsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class HomeScreenRelatedAPIsUseCase(private val homeScreenRelatedAPIsRepository: HomeScreenRelatedAPIsRepository = HomeScreenRelatedAPIsRelatedAPIsImplementation()) {
    fun apodData(): Flow<NetworkState<APODDTO>> {
        return flow {
            try {
                emit(NetworkState.Loading(""))
                emit(NetworkState.Success(homeScreenRelatedAPIsRepository.getAPODDataFromTheAPI()))
            } catch (e: Exception) {
                emit(NetworkState.Failure(e.message.toString()))
            }
        }
    }

    fun epicData(coroutineScope: CoroutineScope): Flow<NetworkState<List<EPICSpecificDTO>>> {
        return flow {
            try {
                emit(NetworkState.Loading(""))
                val epicData = mutableListOf<Deferred<EPICSpecificDTO>>()
                val epicDataJob = coroutineScope.launch {
                    homeScreenRelatedAPIsRepository.getEpicDataForASpecificDate(
                        homeScreenRelatedAPIsRepository.getAllEpicDataDates().first().date
                    ).forEach {
                        val modifiedData = async {
                            jetSpacerLog(it.date.substringBefore(" ").replace("-", "/"))
                            jetSpacerLog(it.image)
                            it.copy(
                                image = "https://epic.gsfc.nasa.gov/archive/natural/${
                                    it.date.substringBefore(" ").replace("-", "/")
                                }/png/${it.image}.png"
                            )
                        }
                        epicData.add(modifiedData)
                    }
                }
                epicDataJob.join()
                emit(NetworkState.Success(epicData.awaitAll()))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(NetworkState.Failure(e.message.toString()))
            }
        }
    }
}