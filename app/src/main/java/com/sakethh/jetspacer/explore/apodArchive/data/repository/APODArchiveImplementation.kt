package com.sakethh.jetspacer.explore.apodArchive.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.common.utils.Constants
import com.sakethh.jetspacer.explore.apodArchive.domain.repository.APODArchiveRepository
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class APODArchiveImplementation : APODArchiveRepository {
    override suspend fun getAPODArchiveData(startDate: String, endDate: String): HttpResponse {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/planetary/apod?api_key=${Constants.NASA_API_KEY}&start_date=$endDate&end_date=$startDate")
    }
}