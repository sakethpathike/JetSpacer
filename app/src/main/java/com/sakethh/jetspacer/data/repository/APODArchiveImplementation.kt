package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.common.Network
import com.sakethh.jetspacer.domain.repository.APODArchiveRepository
import com.sakethh.jetspacer.ui.GlobalSettings
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class APODArchiveImplementation : APODArchiveRepository {
    override suspend fun getAPODArchiveData(startDate: String, endDate: String): HttpResponse {
        return Network.ktorClient.get("https://api.nasa.gov/planetary/apod?api_key=${GlobalSettings.nasaAPIKey.value}&start_date=$endDate&end_date=$startDate")
    }
}