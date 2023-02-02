package com.sakethh.jetspacer.screens.home.data.remote.apod

import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM

class APODFetching(){
    suspend fun getAPOD(): APOD_DTO {
        val apodImplementation=APODImplementation(ktorClient= HTTPClient.ktorClientWithCache, apodURL = "https://api.nasa.gov/planetary/apod?api_key=${BookMarksVM.dbImplementation.localDBData().getAPIKeys()[0].currentNASAAPIKey}")
        return apodImplementation.getAPOD()
    }
}