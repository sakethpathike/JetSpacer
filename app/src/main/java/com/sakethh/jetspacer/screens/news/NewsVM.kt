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

    val _topHeadLinesListFromAPI = mutableStateOf<List<Article>>(emptyList())
    val topHeadLinesListFromAPI = mutableStateOf<List<Article>>(emptyList())
    val totalNewsCount = mutableStateOf(0)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                loadTopHeadLinesData()
            }
        }
    }

    suspend fun loadTopHeadLinesData() {
        NewsData.currentPage++
        coroutineScope {
            awaitAll(async {
                _topHeadLinesListFromAPI.value = newsRepo.getCustomNewsList(page = NewsData.currentPage).component1()
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