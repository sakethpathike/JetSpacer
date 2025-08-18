package com.sakethh.jetspacer.domain.repository

import io.ktor.client.statement.HttpResponse

interface NasaRepository {
    suspend fun fetchApod(): HttpResponse
    suspend fun fetchApodArchive(startDate: String, endDate: String): HttpResponse

    suspend fun fetchEpicImages(date: String): HttpResponse
    suspend fun fetchEpicAvailableDates(): HttpResponse

    suspend fun searchImageLibrary(query: String, page: Int): HttpResponse
    suspend fun fetchImageLibraryAssets(assetId: String): HttpResponse

    suspend fun fetchLatestRoverImages(roverName: String): HttpResponse
    suspend fun fetchRoverImages(
        roverName: String,
        cameraName: String?,
        sol: Int?,
        page: Int = 1
    ): HttpResponse
}