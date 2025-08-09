package com.sakethh.jetspacer.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.data.repository.TopHeadlinesDataImplementation
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.repository.TopHeadlinesDataRepository
import com.sakethh.jetspacer.domain.useCase.FetchCurrentAPODUseCase
import com.sakethh.jetspacer.domain.useCase.FetchCurrentEPICDataUseCase
import com.sakethh.jetspacer.domain.useCase.FetchRemoteTopHeadlinesUseCase
import com.sakethh.jetspacer.ui.screens.headlines.NewsScreenState
import com.sakethh.jetspacer.ui.screens.home.state.apod.APODState
import com.sakethh.jetspacer.ui.screens.home.state.apod.ModifiedAPODDTO
import com.sakethh.jetspacer.ui.screens.home.state.epic.EPICState
import com.sakethh.jetspacer.ui.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.ui.utils.uiEvent.UiChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeScreenViewModel(
    private val topHeadlinesRepository: TopHeadlinesDataRepository = TopHeadlinesDataImplementation(),
    private val fetchRemoteTopHeadlinesUseCase: FetchRemoteTopHeadlinesUseCase = FetchRemoteTopHeadlinesUseCase(),
    fetchCurrentAPODUseCase: FetchCurrentAPODUseCase = FetchCurrentAPODUseCase(),
    fetchCurrentEPICDataUseCase: FetchCurrentEPICDataUseCase = FetchCurrentEPICDataUseCase()
) :
    ViewModel() {
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
    fun retrievePaginatedTopHeadlines() {
        newsAPIJob?.cancel()
        newsAPIJob = fetchRemoteTopHeadlinesUseCase(10, ++currentPage).cancellable().onEach {
            when (val topHeadLinesData = it) {
                is Response.Failure -> {
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

                is Response.Loading -> {
                    topHeadLinesState.value =
                        topHeadLinesState.value.copy(isLoading = true, error = false)
                }

                is Response.Success -> {
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
    val apodState = mutableStateOf(
        APODState(
            isLoading = true, error = false, apod = ModifiedAPODDTO(
                copyright = "",
                date = "",
                explanation = "",
                hdUrl = "",
                mediaType = "",
                title = "",
                url = ""
            ),
            statusCode = 0,
            statusDescription = ""
        )
    )

    var epicState by mutableStateOf(
        EPICState(
            data = listOf(),
            isLoading = false,
            error = false,
            statusCode = 0,
            statusDescription = ""
        )
    )

    init {
        retrievePaginatedTopHeadlines()
        fetchCurrentAPODUseCase().onEach {
            when (val apodData = it) {
                is Response.Failure -> {
                    apodState.value = apodState.value.copy(
                        isLoading = false,
                        error = true,
                        statusCode = apodData.statusCode,
                        statusDescription = apodData.statusDescription
                    )
                    UiChannel.pushUiEvent(
                        uiEvent = UIEvent.ShowSnackbar(apodData.exceptionMessage),
                        coroutineScope = this.viewModelScope
                    )
                }

                is Response.Loading -> {
                    apodState.value = apodState.value.copy(isLoading = true, error = false)
                }

                is Response.Success -> {
                    apodState.value = apodState.value.copy(
                        isLoading = false,
                        error = false,
                        apod = ModifiedAPODDTO(
                            copyright = apodData.data.copyright ?: "",
                            date = apodData.data.date ?: "",
                            explanation = apodData.data.explanation ?: "",
                            hdUrl = apodData.data.hdUrl ?: "",
                            mediaType = apodData.data.mediaType ?: "",
                            title = apodData.data.title ?: "",
                            url = apodData.data.url ?: ""
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)

        fetchCurrentEPICDataUseCase().onEach {
            when (val epicData = it) {
                is Response.Failure -> {
                    epicState = epicState.copy(
                        isLoading = false,
                        error = true,
                        statusCode = epicData.statusCode,
                        statusDescription = epicData.statusDescription
                    )
                    UiChannel.pushUiEvent(
                        uiEvent = UIEvent.ShowSnackbar(epicData.exceptionMessage),
                        coroutineScope = this.viewModelScope
                    )
                }

                is Response.Loading -> {
                    epicState = epicState.copy(isLoading = true)
                }

                is Response.Success -> {
                    epicState = epicState.copy(isLoading = false, data = epicData.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}