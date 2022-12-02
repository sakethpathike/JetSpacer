package com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation

import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto.IPGeoLocationDTO
import com.sakethh.jetspacer.httpClient.HTTPClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IPGeolocationFetching(
    private val ipgGeoLocationImplementation: IPGeoLocationImplementation = IPGeoLocationImplementation(httpClient = HTTPClient.ktorClient)
) {
    suspend fun getGeoLocationData(): IPGeoLocationDTO {
        return ipgGeoLocationImplementation.getGeoLocationData()
    }

    suspend fun getAstronomicalData(): Flow<IPGeoLocationDTO> {
        return flow {
            while (true) {
                emit(ipgGeoLocationImplementation.getGeoLocationData())
                kotlinx.coroutines.delay(1500L)
            }
        }
    }
}