package com.sakethh.jetspacer.explore.apodArchive.domain.repository

import com.sakethh.jetspacer.home.domain.model.APODDTO

interface APODArchiveRepository {
    suspend fun getAPODArchiveData(startDate: String, endDate: String): List<APODDTO>
}