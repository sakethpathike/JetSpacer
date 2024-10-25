package com.sakethh.jetspacer.explore.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.explore.domain.model.api.NASAImageLibrarySearchDTO
import com.sakethh.jetspacer.explore.domain.repository.NASAImageLibrarySearchRepository
import io.ktor.client.call.body
import io.ktor.client.request.get

class NASAImageLibrarySearchImplementation : NASAImageLibrarySearchRepository {
    override suspend fun getResultsFromNASAImageLibrary(
        query: String,
        page: Int
    ): NASAImageLibrarySearchDTO {
        return HTTPClient.ktorClient.get("https://images-api.nasa.gov/search?q=$query&page=$page")
            .body()
    }

    override suspend fun getImagesFromImageLibraryWithNasaId(id: String): List<String> {
        return HTTPClient.ktorClient.get("https://images-assets.nasa.gov/image/$id/collection.json")
            .body()
    }
}