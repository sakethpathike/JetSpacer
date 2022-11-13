package com.sakethh.jetspacer.screens.home.data.remote.issLocation

import com.sakethh.jetspacer.screens.KtorClient
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.ISSLocationDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ISSLocationFetching() {
    suspend fun getISSLatitudeAndLongitude(): Flow<ISSLocationDTO> {
        val issLocationImplementation = ISSLocationImplementation(KtorClient.httpClient)
        return flow {
            while (true) {
                emit(issLocationImplementation.getISSLocation())
                kotlinx.coroutines.delay(1500)
            }
        }
    }
}