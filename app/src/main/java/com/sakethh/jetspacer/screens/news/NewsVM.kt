package com.sakethh.jetspacer.screens.news

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.screens.news.dto.Article
import kotlinx.coroutines.*

class NewsVM(private val newsRepo: NewsRepo = NewsRepo()) : ViewModel() {

    val _topHeadLinesListFromAPI = mutableStateOf<List<Article>>(emptyList())
    val topHeadLinesListFromAPI = mutableStateOf<List<Article>>(emptyList())
    val totalNewsCount = mutableStateOf(0)
    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }

    companion object {
        val newsBottomSheetContentImpl = mutableStateOf(
            NewsBottomSheetMutableStateDTO(
                mutableStateOf(""),
                mutableStateOf(""),
                mutableStateOf(""),
                mutableStateOf(""),
                mutableStateOf("")
            )
        )
    }

    init {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            loadTopHeadLinesData()
        }
    }

    suspend fun loadTopHeadLinesData() {
        coroutineScope {
            awaitAll(async {
                _topHeadLinesListFromAPI.value =
                    newsRepo.getCustomNewsList(page = NewsData.currentPage).component1()
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
        var currentPage = 1
    }
}