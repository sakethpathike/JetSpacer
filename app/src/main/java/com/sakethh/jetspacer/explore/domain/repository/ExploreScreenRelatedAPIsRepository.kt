package com.sakethh.jetspacer.explore.domain.repository

import com.sakethh.jetspacer.explore.domain.model.api.iss.source.ISSLocationDTO
import com.sakethh.jetspacer.explore.domain.model.api.nasa.NASAImageLibrarySearchDTO

interface ExploreScreenRelatedAPIsRepository {
    suspend fun getResultsFromNASAImageLibrary(
        query: String,
        page: Int
    ): NASAImageLibrarySearchDTO

    suspend fun getImagesFromImageLibraryWithNasaId(id: String): List<String>

    suspend fun getISSLocation(): ISSLocationDTO
}