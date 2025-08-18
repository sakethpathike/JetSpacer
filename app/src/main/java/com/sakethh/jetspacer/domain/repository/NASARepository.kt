package com.sakethh.jetspacer.domain.repository

import com.sakethh.jetspacer.domain.Response
import io.ktor.client.statement.HttpResponse

interface NasaRepository {
    suspend fun fetchApod(): Response<HttpResponse>
    suspend fun fetchApodArchive(startDate: String, endDate: String): Response<HttpResponse>

    suspend fun fetchEpicImages(date: String): Response<HttpResponse>
    suspend fun fetchEpicAvailableDates(): Response<HttpResponse>

    suspend fun searchImageLibrary(query: String, page: Int): Response<HttpResponse>
    suspend fun fetchImageLibraryAssets(assetId: String): Response<HttpResponse>

    suspend fun fetchLatestRoverImages(roverName: String): Response<HttpResponse>
    suspend fun fetchRoverImages(
        roverName: String,
        cameraName: String?,
        sol: Int?,
        page: Int = 1
    ): Response<HttpResponse>
}