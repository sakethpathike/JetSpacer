package com.sakethh.jetspacer.screens.home.data.remote.issLocation

import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.ISSLocationDTO
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.IssPosition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class ISSLocationFetching() {
    suspend fun getISSLatitudeAndLongitude(): Flow<ISSLocationDTO> {
        val issLocationImplementation = ISSLocationImplementation(ktorClient = HTTPClient.ktorClientWithCache)
        return try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            flow {
                while (true) {
                    emit(issLocationImplementation.getISSLocation())
                    kotlinx.coroutines.delay(1500)
                }
            }
        }catch (_:Exception){
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            MutableStateFlow(ISSLocationDTO(IssPosition("",""),"",0))
        }
    }
}