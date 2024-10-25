package com.sakethh.jetspacer.explore.domain.repository

import com.sakethh.jetspacer.explore.domain.model.NASAImageLibrarySearchDTO

interface NASAImageLibrarySearchRepository {
    suspend fun getResultsFromNASAImageLibrary(
        query: String,
        page: Int
    ): NASAImageLibrarySearchDTO
}