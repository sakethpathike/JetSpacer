package com.sakethh.jetspacer.screens.home.data.remote.apod

import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class APODImplementation(private val ktorClient:HttpClient):APODService {
    override suspend fun getAPOD(): APOD_DTO {
        return ktorClient.get(Constants.APOD_URL).body()
    }
}