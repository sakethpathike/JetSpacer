package com.sakethh.jetspacer.screens.news

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.CurrentHTTPCodes
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.news.dto.Article
import com.sakethh.jetspacer.screens.news.dto.NewsDTO
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class NewsRepo {
    suspend fun getCustomNewsList(page: Int): List<List<Article>> {
        val _data = mutableStateListOf<Deferred<List<Article>>>()
        val client = try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            val httpResponse = HTTPClient.ktorClientWithoutCache.get(
                "https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=10&page=$page&apiKey=${
                    BookMarksVM.dbImplementation.localDBData().getAPIKeys()[0].currentNewsAPIKey
                }"
            )
            coroutineScope {
                awaitAll(
                    async {
                        CurrentHTTPCodes.newsAPICurrentHttpCode.value = httpResponse.status.value
                    },
                    async {
                        NewsVM.NewsData.totalResults = httpResponse.body<NewsDTO>().totalResults
                    })
            }
            httpResponse.body()
        } catch (_: Exception) {
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            NewsDTO()
        }
        coroutineScope {
            val data = async {
                try {
                    HomeScreenViewModel.Network.isConnectionSucceed.value = true
                    client.articles
                } catch (_: Exception) {
                    HomeScreenViewModel.Network.isConnectionSucceed.value = false
                    emptyList()
                }
            }
            _data.add(data)
        }
        return _data.awaitAll()
    }
}