package com.sakethh.jetspacer.domain.repository

import io.ktor.client.statement.HttpResponse

interface ExploreScreenRelatedAPIsRepository {
    suspend fun getResultsFromNASAImageLibrary(
        query: String,
        page: Int
    ): HttpResponse

    suspend fun getImagesFromImageLibraryWithNasaId(id: String): HttpResponse

    suspend fun getISSLocation(): HttpResponse
}