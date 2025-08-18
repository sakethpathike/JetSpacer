package com.sakethh.jetspacer.domain.repository

import io.ktor.client.statement.HttpResponse

interface ISSInfoRepo {
    suspend fun getISSLocation(): HttpResponse
}