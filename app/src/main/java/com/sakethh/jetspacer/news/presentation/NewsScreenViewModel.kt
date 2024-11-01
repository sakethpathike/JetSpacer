package com.sakethh.jetspacer.news.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UiChannel
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.news.domain.model.NewsDTO
import com.sakethh.jetspacer.news.domain.useCase.TopHeadlinesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NewsScreenViewModel(private val topHeadlinesUseCase: TopHeadlinesUseCase = TopHeadlinesUseCase()) :
    ViewModel() {
    val topHeadLinesState =
        mutableStateOf(
            NewsScreenState(
                isLoading = true,
                data = NewsDTO(),
                error = false,
                reachedMaxHeadlines = false, statusCode = 0, statusDescription = ""
            )
        )

    private var currentPage = 0
    private var newsAPIJob: Job? = null
    init {
        retrievePaginatedTopHeadlines()
    }

    fun retrievePaginatedTopHeadlines() {
        newsAPIJob?.cancel()
        newsAPIJob = topHeadlinesUseCase(10, ++currentPage).cancellable().onEach {
            when (val topHeadLinesData = it) {
                is NetworkState.Failure -> {
                    topHeadLinesState.value =
                        topHeadLinesState.value.copy(
                            isLoading = false,
                            error = true,
                            statusCode = topHeadLinesData.statusCode,
                            statusDescription = topHeadLinesData.statusDescription
                        )
                    UiChannel.pushUiEvent(
                        uiEvent = UIEvent.ShowSnackbar(topHeadLinesData.exceptionMessage),
                        coroutineScope = viewModelScope
                    )
                }

                is NetworkState.Loading -> {
                    topHeadLinesState.value =
                        topHeadLinesState.value.copy(isLoading = true, error = false)
                }

                is NetworkState.Success -> {
                    topHeadLinesState.value = topHeadLinesState.value.copy(
                        isLoading = false,
                        data = topHeadLinesState.value.data.copy(
                            articles = topHeadLinesState.value.data.articles + topHeadLinesData.data.articles,
                            status = topHeadLinesData.data.status,
                            totalResults = topHeadLinesState.value.data.totalResults + topHeadLinesData.data.totalResults
                        ),
                        error = false,
                        reachedMaxHeadlines = topHeadLinesData.data.articles.isEmpty()
                    )
                    if (topHeadLinesData.data.articles.isEmpty()) {
                        jetSpacerLog("found max")
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}