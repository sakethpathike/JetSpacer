package com.sakethh.jetspacer.home.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.home.domain.repository.HomeScreenRelatedAPIsRepository
import com.sakethh.jetspacer.home.settings.presentation.utils.GlobalSettings
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class HomeScreenRelatedAPIsRelatedAPIsImplementation : HomeScreenRelatedAPIsRepository {
    override suspend fun getAPODDataFromTheAPI(): HttpResponse {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/planetary/apod?api_key=${GlobalSettings.nasaAPIKey.value}")
    }

    override suspend fun getEpicDataForASpecificDate(date: String): HttpResponse {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/EPIC/api/natural/date/$date?api_key=${GlobalSettings.nasaAPIKey.value}")
    }

    override suspend fun getAllEpicDataDates(): HttpResponse {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/EPIC/api/natural?api_key=${GlobalSettings.nasaAPIKey.value}")
    }
}