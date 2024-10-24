package com.sakethh.jetspacer.news.data.repository

import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.news.domain.model.NewsDTO
import com.sakethh.jetspacer.news.domain.repository.NewsDataRepository
import io.ktor.client.call.body
import io.ktor.client.request.get

class NewsDataImplementation : NewsDataRepository {
    override suspend fun getTopHeadLines(): NewsDTO {
        return HTTPClient.ktorClient.get("https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=10&page=1&apiKey=27d2f9412f1148e6ba7b544336844ced")
            .body()
    }
}