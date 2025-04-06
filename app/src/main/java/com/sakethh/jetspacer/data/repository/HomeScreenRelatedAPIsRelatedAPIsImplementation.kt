package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.common.Network
import com.sakethh.jetspacer.domain.repository.HomeScreenRelatedAPIsRepository
import com.sakethh.jetspacer.ui.GlobalSettings
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class HomeScreenRelatedAPIsRelatedAPIsImplementation : HomeScreenRelatedAPIsRepository {
    override suspend fun getAPODDataFromTheAPI(): HttpResponse {
        return Network.ktorClient.get("https://api.nasa.gov/planetary/apod?api_key=${GlobalSettings.nasaAPIKey.value}")
    }

    override suspend fun getEpicDataForASpecificDate(date: String): HttpResponse {
        return Network.ktorClient.get("https://api.nasa.gov/EPIC/api/natural/date/$date?api_key=${GlobalSettings.nasaAPIKey.value}")
    }

    override suspend fun getAllEpicDataDates(): HttpResponse {
        return Network.ktorClient.get("https://api.nasa.gov/EPIC/api/natural?api_key=${GlobalSettings.nasaAPIKey.value}")
    }
}