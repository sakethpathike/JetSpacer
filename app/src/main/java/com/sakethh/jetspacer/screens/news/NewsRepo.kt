package com.sakethh.jetspacer.screens.news

import androidx.compose.runtime.mutableStateListOf
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.news.dto.Article
import com.sakethh.jetspacer.screens.news.dto.NewsDTO
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class NewsRepo : NewsService {
    override suspend fun getCustomNewsList(page: Int): List<List<Article>> {
        val _data = mutableStateListOf<Deferred<List<Article>>>()
        coroutineScope {
            val data = async {
                try {
                    HomeScreenViewModel.Network.isConnectionSucceed.value = true
                    HTTPClient.ktorClientWithoutCache.get("https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=10&page=$page&apiKey=${Constants.NEWS_API_API_KEY}")
                        .body<NewsDTO>().articles
                } catch (_: Exception) {
                    HomeScreenViewModel.Network.isConnectionSucceed.value = false
                    emptyList()
                }
            }
            _data.add(data)
        }
        return _data.awaitAll()
    }

    override suspend fun getStatus(): String {
        return try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            HTTPClient.ktorClientWithoutCache.get("https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=10&page=1&apiKey=${Constants.NEWS_API_API_KEY}")
                .body<NewsDTO>().status
        } catch (_: Exception) {
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            ""
        }
    }

    override suspend fun totalResults(): Int {
        return try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            HTTPClient.ktorClientWithoutCache.get("https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=10&page=1&apiKey=${Constants.NEWS_API_API_KEY}")
                .body<NewsDTO>().totalResults
        } catch (_: Exception) {
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            0
        }
    }
}

interface NewsService {
    suspend fun getCustomNewsList(page: Int): List<List<Article>>
    suspend fun getStatus(): String
    suspend fun totalResults(): Int
}