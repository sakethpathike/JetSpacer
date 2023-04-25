package com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation

import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.CurrentHTTPCodes
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto.IPGeoLocationDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class IPGeoLocationImplementation(private val httpClient: HttpClient) : IPGeolocationService {
    override suspend fun getGeoLocationData(): IPGeoLocationDTO {
        val httpResponse = httpClient.get("https://api.ipgeolocation.io/astronomy?apiKey=${BookMarksVM.dbImplementation.localDBData().getAPIKeys()[0].currentIPGeoLocationAPIKey}")
        CurrentHTTPCodes.ipGeoLocationCurrentHttpCode.value = httpResponse.status.value
        return  httpResponse.body()
    }
}