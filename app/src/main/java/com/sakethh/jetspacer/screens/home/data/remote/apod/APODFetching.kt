package com.sakethh.jetspacer.screens.home.data.remote.apod

import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.httpClient.HTTPClient

class APODFetching {
    suspend fun getAPOD(): APOD_DTO {
        val apodImplementation=APODImplementation(ktorClient= HTTPClient.ktorClient)
        return apodImplementation.getAPOD()
    }
}