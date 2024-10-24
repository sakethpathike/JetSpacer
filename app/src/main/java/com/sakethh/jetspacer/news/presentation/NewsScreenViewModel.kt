package com.sakethh.jetspacer.news.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.presentation.utils.UIEvent
import com.sakethh.jetspacer.common.presentation.utils.UiChannel
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.news.domain.model.NewsDTO
import com.sakethh.jetspacer.news.domain.useCase.TopHeadlinesUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NewsScreenViewModel(private val topHeadlinesUseCase: TopHeadlinesUseCase = TopHeadlinesUseCase()) :
    ViewModel() {
    val topHeadLinesState =
        mutableStateOf(NewsScreenState(isLoading = true, data = NewsDTO(), error = false))

    init {
        jetSpacerLog("yeahh")
        retrieveTopHeadLines()
    }

    private fun retrieveTopHeadLines() {
        topHeadlinesUseCase().onEach {
            when (val topHeadLinesData = it) {
                is NetworkState.Failure -> {
                    jetSpacerLog("failed")
                    topHeadLinesState.value =
                        topHeadLinesState.value.copy(isLoading = false, error = true)
                    UiChannel.pushUiEvent(
                        uiEvent = UIEvent.ShowToast(topHeadLinesData.msg),
                        coroutineScope = viewModelScope
                    )
                }

                is NetworkState.Loading -> {
                    jetSpacerLog("loading")
                    topHeadLinesState.value =
                        topHeadLinesState.value.copy(isLoading = true, error = false)
                }

                is NetworkState.Success -> {
                    jetSpacerLog("sucess")
                    topHeadLinesState.value = topHeadLinesState.value.copy(
                        isLoading = false,
                        data = topHeadLinesData.data,
                        error = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}