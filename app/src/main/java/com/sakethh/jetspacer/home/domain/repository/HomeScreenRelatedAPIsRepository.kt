package com.sakethh.jetspacer.home.domain.repository

import io.ktor.client.statement.HttpResponse

interface HomeScreenRelatedAPIsRepository {
    suspend fun getAPODDataFromTheAPI(): HttpResponse

    suspend fun getEpicDataForASpecificDate(date: String): HttpResponse

    suspend fun getAllEpicDataDates(): HttpResponse
}