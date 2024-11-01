package com.sakethh.jetspacer.news.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.home.settings.presentation.utils.GlobalSettings
import com.sakethh.jetspacer.news.domain.repository.NewsDataRepository
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class NewsDataImplementation : NewsDataRepository {
    override suspend fun getTopHeadLines(pageSize: Int, page: Int): HttpResponse {
        return HTTPClient.ktorClient.get("https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=$pageSize&page=$page&apiKey=${GlobalSettings.newsApiAPIKey.value}")
    }
}