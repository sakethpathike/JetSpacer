package com.sakethh.jetspacer.headlines.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.data.local.data.repository.TopHeadlineCacheImplementation
import com.sakethh.jetspacer.common.data.local.domain.repository.TopHeadlinesCacheRepository
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UiChannel
import com.sakethh.jetspacer.headlines.data.repository.TopHeadlinesDataImplementation
import com.sakethh.jetspacer.headlines.domain.repository.TopHeadlinesDataRepository
import com.sakethh.jetspacer.headlines.domain.useCase.FetchRemoteTopHeadlinesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TopHeadlinesScreenViewModel(
    private val fetchRemoteTopHeadlinesUseCase: FetchRemoteTopHeadlinesUseCase = FetchRemoteTopHeadlinesUseCase(),
    private val topHeadlinesDataRepository: TopHeadlinesDataRepository = TopHeadlinesDataImplementation(),
    private val topHeadlinesCacheRepository: TopHeadlinesCacheRepository = TopHeadlineCacheImplementation()
) : TopHeadlineDetailScreenViewmodel() {
    val topHeadLinesState =
        mutableStateOf(
            NewsScreenState(
                isLoading = true,
                data = emptyList(),
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
        newsAPIJob = fetchRemoteTopHeadlinesUseCase(10, ++currentPage).cancellable().onEach {
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
                        data = topHeadLinesData.data.dropLastWhile {
                            it.id == (-1).toLong()
                        },
                        error = false,
                        reachedMaxHeadlines = try {
                            topHeadLinesData.data.last().id == (-1).toLong()
                        } catch (_: Exception) {
                            false
                        }
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun clearCacheAndRefresh() {
        viewModelScope.launch {
            topHeadLinesState.value = topHeadLinesState.value.copy(
                reachedMaxHeadlines = false,
                isLoading = true,
                statusCode = 0
            )
            topHeadlinesDataRepository.clearCache()
            topHeadlinesCacheRepository.setEndReached(false)
            currentPage = 0
            retrievePaginatedTopHeadlines()
        }
    }
}