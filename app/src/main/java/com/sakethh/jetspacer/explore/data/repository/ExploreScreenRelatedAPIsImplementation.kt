package com.sakethh.jetspacer.explore.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.explore.domain.repository.ExploreScreenRelatedAPIsRepository
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class ExploreScreenRelatedAPIsImplementation : ExploreScreenRelatedAPIsRepository {
    override suspend fun getResultsFromNASAImageLibrary(
        query: String,
        page: Int
    ): HttpResponse {
        return HTTPClient.ktorClient.get("https://images-api.nasa.gov/search?q=$query&page=$page")
    }

    override suspend fun getImagesFromImageLibraryWithNasaId(id: String): HttpResponse {
        return HTTPClient.ktorClient.get("https://images-assets.nasa.gov/image/$id/collection.json")
    }

    override suspend fun getISSLocation(): HttpResponse {
        return HTTPClient.ktorClient.get("http://api.open-notify.org/iss-now.json")
    }
}