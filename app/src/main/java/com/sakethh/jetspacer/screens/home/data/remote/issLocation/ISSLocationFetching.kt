package com.sakethh.jetspacer.screens.home.data.remote.issLocation

import android.util.Log
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
                    Log.d("Logs for ipgeolocation",issLocationImplementation.getISSLocation().iss_position.latitude.toString())
                    kotlinx.coroutines.delay(5000)
                }
            }
        }catch (_:Exception){
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            MutableStateFlow(ISSLocationDTO(IssPosition("",""),"",0))
        }
    }
}