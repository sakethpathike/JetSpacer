package com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation

import android.util.Log
import com.sakethh.jetspacer.screens.HTTPClient.KtorClient.httpClient
import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto.IPGeoLocationDTO
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IPGeolocationFetching(
    private val ipgGeoLocationImplementation: IPGeoLocationImplementation = IPGeoLocationImplementation(
        httpClient = httpClient
    )
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