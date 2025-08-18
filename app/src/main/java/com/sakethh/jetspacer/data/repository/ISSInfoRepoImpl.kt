package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.domain.repository.ISSInfoRepo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class ISSInfoRepoImpl(private val httpClient: HttpClient) : ISSInfoRepo {
    override suspend fun getISSLocation(): HttpResponse {
        return httpClient.get("http://api.open-notify.org/iss-now.json")
    }
}