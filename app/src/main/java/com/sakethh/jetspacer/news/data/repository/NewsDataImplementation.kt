package com.sakethh.jetspacer.news.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.common.utils.Constants
import com.sakethh.jetspacer.news.domain.model.NewsDTO
import com.sakethh.jetspacer.news.domain.repository.NewsDataRepository
import io.ktor.client.call.body
import io.ktor.client.request.get

class NewsDataImplementation : NewsDataRepository {
    override suspend fun getTopHeadLines(pageSize: Int, page: Int): NewsDTO {
        return HTTPClient.ktorClient.get("https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=$pageSize&page=$page&apiKey=${Constants.NEWS_API_KEY}")
            .body()
    }
}