package com.sakethh.jetspacer.screens.home.data.remote.apod

import com.sakethh.jetspacer.screens.HTTPClient
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO

class APODFetching {
    suspend fun getAPOD(): APOD_DTO {
        val apodImplementation=APODImplementation(HTTPClient.KtorClient.httpClient)
        return apodImplementation.getAPOD()
    }
}