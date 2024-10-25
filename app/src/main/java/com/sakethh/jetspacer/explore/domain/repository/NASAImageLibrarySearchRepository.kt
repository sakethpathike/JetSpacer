package com.sakethh.jetspacer.explore.domain.repository

import com.sakethh.jetspacer.explore.domain.model.api.NASAImageLibrarySearchDTO

interface NASAImageLibrarySearchRepository {
    suspend fun getResultsFromNASAImageLibrary(
        query: String,
        page: Int
    ): NASAImageLibrarySearchDTO

    suspend fun getImagesFromImageLibraryWithNasaId(id: String): List<String>
}