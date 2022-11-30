package com.sakethh.jetspacer.screens.space.remote.data

import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.httpClient.HTTPClient.ktorClient
import io.ktor.client.call.*
import io.ktor.client.request.*


interface SpaceScreenService {
    suspend fun getAPODSpecificDateData(apodDateForURL: String): APOD_DTO
}

class SpaceScreenImplementation : SpaceScreenService {
    override suspend fun getAPODSpecificDateData(apodDateForURL: String): APOD_DTO {
        return ktorClient.get("${Constants.APOD_URL}&date=$apodDateForURL").body()
    }

}