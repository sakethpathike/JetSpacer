package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.headlines.HeadlinesDTO
import com.sakethh.jetspacer.domain.repository.HeadlinesRepository
import com.sakethh.jetspacer.ui.AppPreferences
import com.sakethh.jetspacer.ui.utils.extractBodyFlow
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HeadlinesRepoImpl(
    private val httpClient: HttpClient,
) : HeadlinesRepository {

    override suspend fun getTopHeadLines(
        pageSize: Int, page: Int
    ): Flow<Response<HeadlinesDTO>> {

        val httpResult: Response<HttpResponse> = try {
            val httpResponse =
                httpClient.get("https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=$pageSize&page=$page&apiKey=${AppPreferences.newsApiAPIKey.value}")
            Response.Success(httpResponse)
        } catch (e: Exception) {
            Response.Failure.clientFailure(e.message ?: "")
        }

        return extractBodyFlow(httpResult = httpResult) { httpResponse ->
            httpResponse.body()
        }
    }
}