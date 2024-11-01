package com.sakethh.jetspacer.explore.apodArchive.domain.repository

import io.ktor.client.statement.HttpResponse

interface APODArchiveRepository {
    suspend fun getAPODArchiveData(startDate: String, endDate: String): HttpResponse
}