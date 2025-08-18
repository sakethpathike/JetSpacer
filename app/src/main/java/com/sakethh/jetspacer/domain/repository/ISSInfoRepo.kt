package com.sakethh.jetspacer.domain.repository

import com.sakethh.jetspacer.domain.Response
import io.ktor.client.statement.HttpResponse

interface ISSInfoRepo {
    suspend fun getISSLocation(): Response<HttpResponse>
}