package com.sakethh.jetspacer.screens.home.data.remote.issLocation

import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.ISSLocationDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ISSLocationImplementation(private val ktorClient:HttpClient):ISSLocationService {
    override suspend fun getISSLocation(): ISSLocationDTO {
        return ktorClient.get(Constants.ISS_LOCATION_URL).body()
    }
}