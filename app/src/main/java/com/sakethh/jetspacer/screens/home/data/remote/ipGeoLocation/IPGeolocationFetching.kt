package com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation

import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto.IPGeoLocationDTO
import com.sakethh.jetspacer.screens.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class IPGeolocationFetching(
    private val ipgGeoLocationImplementation: IPGeoLocationImplementation = IPGeoLocationImplementation(httpClient = HTTPClient.ktorClientWithCache)
) {
    suspend fun getGeoLocationData(): IPGeoLocationDTO {
        return try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            ipgGeoLocationImplementation.getGeoLocationData()
        }catch (_:Exception){
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            IPGeoLocationDTO()
        }
    }

    suspend fun getAstronomicalData(): Flow<IPGeoLocationDTO> {
        return try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            flow {
                while (true) {
                    emit(ipgGeoLocationImplementation.getGeoLocationData())
                    kotlinx.coroutines.delay(
                        "${Settings.astronomicalTimeInterval.value}000".toInt().toLong()
                    )
                }
            }
        }catch (_:Exception){
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            MutableStateFlow(IPGeoLocationDTO())
        }
    }
}