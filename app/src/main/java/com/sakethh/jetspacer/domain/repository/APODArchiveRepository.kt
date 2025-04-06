package com.sakethh.jetspacer.domain.repository

import io.ktor.client.statement.HttpResponse

interface APODArchiveRepository {
    suspend fun getAPODArchiveData(startDate: String, endDate: String): HttpResponse
}