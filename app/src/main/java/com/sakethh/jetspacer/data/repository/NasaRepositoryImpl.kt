package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.repository.NasaRepository
import com.sakethh.jetspacer.ui.AppPreferences
import com.sakethh.jetspacer.ui.utils.withHttpResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class NasaRepositoryImpl(private val httpClient: HttpClient) : NasaRepository {
    override suspend fun fetchApod(): Response<HttpResponse> {
        return withHttpResponse {
            httpClient.get("https://api.nasa.gov/planetary/apod?api_key=${AppPreferences.nasaAPIKey.value}")
        }
    }

    override suspend fun fetchApodArchive(
        startDate: String, endDate: String
    ): Response<HttpResponse> {
        return withHttpResponse {
            httpClient.get("https://api.nasa.gov/planetary/apod?api_key=${AppPreferences.nasaAPIKey.value}&start_date=$endDate&end_date=$startDate")
        }
    }

    override suspend fun fetchEpicImages(date: String): Response<HttpResponse> {
        return withHttpResponse {
            httpClient.get("https://api.nasa.gov/EPIC/api/natural/date/$date?api_key=${AppPreferences.nasaAPIKey.value}")
        }
    }

    override suspend fun fetchEpicAvailableDates(): Response<HttpResponse> {
        return withHttpResponse {
            httpClient.get("https://api.nasa.gov/EPIC/api/natural?api_key=${AppPreferences.nasaAPIKey.value}")
        }
    }

    override suspend fun searchImageLibrary(
        query: String, page: Int
    ): Response<HttpResponse> {
        return withHttpResponse {
            httpClient.get("https://images-api.nasa.gov/search?q=$query&page=$page")
        }
    }

    override suspend fun fetchImageLibraryAssets(assetId: String): Response<HttpResponse> {
        return withHttpResponse {
            httpClient.get("https://images-assets.nasa.gov/image/$assetId/collection.json")
        }
    }

    override suspend fun fetchLatestRoverImages(roverName: String): Response<HttpResponse> {
        return withHttpResponse {
            httpClient.get("https://api.nasa.gov/mars-photos/api/v1/rovers/$roverName/latest_photos?api_key=${AppPreferences.nasaAPIKey.value}")
        }
    }

    override suspend fun fetchRoverImages(
        roverName: String, cameraName: String?, sol: Int?, page: Int
    ): Response<HttpResponse> {
        return withHttpResponse {
            httpClient.get("https://api.nasa.gov/mars-photos/api/v1/rovers/$roverName/photos?sol=$sol&camera=$cameraName&page=$page&api_key=${AppPreferences.nasaAPIKey.value}")
        }
    }
}