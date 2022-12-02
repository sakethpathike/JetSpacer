package com.sakethh.jetspacer.screens.home.data.remote.issLocation

import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.ISSLocationDTO
import com.sakethh.jetspacer.httpClient.HTTPClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ISSLocationFetching() {
    suspend fun getISSLatitudeAndLongitude(): Flow<ISSLocationDTO> {
        val issLocationImplementation = ISSLocationImplementation(ktorClient = HTTPClient.ktorClient)
        return flow {
            while (true) {
                emit(issLocationImplementation.getISSLocation())
                kotlinx.coroutines.delay(1500)
            }
        }
    }
}