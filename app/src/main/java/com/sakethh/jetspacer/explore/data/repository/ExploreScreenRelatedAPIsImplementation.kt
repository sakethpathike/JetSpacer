package com.sakethh.jetspacer.explore.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.explore.domain.model.api.iss.source.ISSLocationDTO
import com.sakethh.jetspacer.explore.domain.model.api.nasa.NASAImageLibrarySearchDTO
import com.sakethh.jetspacer.explore.domain.repository.ExploreScreenRelatedAPIsRepository
import io.ktor.client.call.body
import io.ktor.client.request.get

class ExploreScreenRelatedAPIsImplementation : ExploreScreenRelatedAPIsRepository {
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

    override suspend fun getISSLocation(): ISSLocationDTO {
        return HTTPClient.ktorClient.get("http://api.open-notify.org/iss-now.json").body()
    }
}