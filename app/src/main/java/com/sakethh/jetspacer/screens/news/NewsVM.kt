package com.sakethh.jetspacer.screens.news

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.screens.news.dto.Article
import com.sakethh.jetspacer.screens.news.dto.Source
import kotlinx.coroutines.*

class NewsVM(private val newsRepo: NewsRepo = NewsRepo()) : ViewModel() {

    private val _topHeadLinesListFromAPI = mutableStateOf<List<Article>>(emptyList())
    val topHeadLinesListFromAPI = mutableStateOf<List<Article>>(emptyList())
    val totalNewsCount = mutableStateOf(3)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                loadTopHeadLinesData()
            }
            while (true) {
                Log.d("news api", topHeadLinesListFromAPI.value.toString())
                delay(1000L)
            }
        }
    }

    suspend fun loadTopHeadLinesData() {
        coroutineScope {
            awaitAll(async {
                NewsData.currentPage = NewsData.currentPage++
                _topHeadLinesListFromAPI.value = listOf(
                    Article(
                        url = "https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=10&page=1&apiKey=${Constants.NEWS_API_API_KEY}",
                        author = "author1",
                        publishedAt = "11-12-12",
                        source = Source(name="source1"),
                        title = "This is title1,This is title1,This is title1,This is title1,This is title1,This is title1,This is title1,This is title1,This is title1,This is title1,"
                    ),
                    Article(
                        "author2",
                        publishedAt = "11-12-12",
                        source = Source(name="source2"),
                        title = "This is title2This is title2This is title2This is title2This is title2This is title2This is title2This is title2This is title2This is title2"
                    ),
                    Article(
                        "author3",
                        publishedAt = "11-12-12",
                        source = Source(name="source3"),
                        title = "This is title3This is title3This is title3This is title3This is title3This is title3This is title3This is title3This is title3This is title3"
                    )
                )
                    /*newsRepo.getCustomNewsList(page = NewsData.currentPage).component1()*/
                topHeadLinesListFromAPI.value += _topHeadLinesListFromAPI.value
            },
                async {
                    totalNewsCount()
                })
        }
    }

    private suspend fun totalNewsCount() {
        totalNewsCount.value = newsRepo.totalResults()
    }

    suspend fun reloadTopHeadLinesData() {
        NewsData.currentPage = 0
        _topHeadLinesListFromAPI.value = emptyList()
        topHeadLinesListFromAPI.value = emptyList()
        loadTopHeadLinesData()
    }

    object NewsData {
        var currentPage = 0
    }
}