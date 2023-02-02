package com.sakethh.jetspacer.screens.space.remote.data

import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.httpClient.HTTPClient.ktorClientWithCache
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.remote.data.marsWeather.dto.MarsWeatherDTO
import io.ktor.client.call.*
import io.ktor.client.request.*


interface SpaceScreenService {
    suspend fun getAPODSpecificDateData(apodDateForURL: String): APOD_DTO
    suspend fun getMarsWeatherData():MarsWeatherDTO
}

class SpaceScreenImplementation : SpaceScreenService {
    override suspend fun getAPODSpecificDateData(apodDateForURL: String): APOD_DTO {
        return try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            ktorClientWithCache.get("https://api.nasa.gov/planetary/apod?api_key=${BookMarksVM.dbImplementation.localDBData().getAPIKeys()[0].currentNASAAPIKey}&date=$apodDateForURL").body()
        }catch (_:Exception){
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            APOD_DTO()
        }
    }

    override suspend fun getMarsWeatherData(): MarsWeatherDTO {
        return  try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            ktorClientWithCache.get(Constants.MARS_WEATHER_URL).body()
        }catch (_:Exception){
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            MarsWeatherDTO()
        }
    }

}