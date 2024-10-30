package com.sakethh.jetspacer.home.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.common.utils.Constants
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.home.domain.model.APODDTO
import com.sakethh.jetspacer.home.domain.model.epic.all.AllEPICDTOItem
import com.sakethh.jetspacer.home.domain.model.epic.specific.EPICSpecificDTO
import com.sakethh.jetspacer.home.domain.repository.HomeScreenRelatedAPIsRepository
import io.ktor.client.call.body
import io.ktor.client.request.get

class HomeScreenRelatedAPIsRelatedAPIsImplementation : HomeScreenRelatedAPIsRepository {
    override suspend fun getAPODDataFromTheAPI(): APODDTO {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/planetary/apod?api_key=${Constants.NASA_API_KEY}")
            .body()
    }

    override suspend fun getEpicDataForASpecificDate(date: String): List<EPICSpecificDTO> {
        jetSpacerLog(date)
        return HTTPClient.ktorClient.get("https://api.nasa.gov/EPIC/api/natural/date/$date?api_key=${Constants.NASA_API_KEY}")
            .body()
    }

    override suspend fun getAllEpicDataDates(): List<AllEPICDTOItem> {
        return HTTPClient.ktorClient.get("https://api.nasa.gov/EPIC/api/natural?api_key=${Constants.NASA_API_KEY}")
            .body()
    }
}