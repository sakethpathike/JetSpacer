package com.sakethh.jetspacer.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.HyleApplication
import com.sakethh.jetspacer.common.Network
import com.sakethh.jetspacer.data.repository.TopHeadlinesDataImplementation
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.Headline
import com.sakethh.jetspacer.domain.onFailure
import com.sakethh.jetspacer.domain.onLoading
import com.sakethh.jetspacer.domain.onSuccess
import com.sakethh.jetspacer.domain.repository.TopHeadlinesDataRepository
import com.sakethh.jetspacer.domain.useCase.FetchCurrentAPODUseCase
import com.sakethh.jetspacer.domain.useCase.FetchCurrentEPICDataUseCase
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
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val topHeadlinesRepository: TopHeadlinesDataRepository = TopHeadlinesDataImplementation(
        Network.ktorClient, topHeadlinesDao = HyleApplication.getLocalDb().topHeadlinesDao
    ),
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

        viewModelScope.launch {
            newsAPIJob = topHeadlinesRepository.getTopHeadLinesFromRemoteAPI(10, ++currentPage).cancellable().onEach {
               it.onLoading {
                   topHeadLinesState.value =
                       topHeadLinesState.value.copy(isLoading = true, error = false)
               }.onFailure {
                   topHeadLinesState.value =
                       topHeadLinesState.value.copy(
                           isLoading = false,
                           error = true,
                           statusCode = it.statusCode,
                           statusDescription = it.statusDescription
                       )
                   UiChannel.pushUiEvent(
                       uiEvent = UIEvent.ShowSnackbar(it.exceptionMessage),
                       coroutineScope = viewModelScope
                   )
               }.onSuccess {
                   topHeadLinesState.value = topHeadLinesState.value.copy(
                       isLoading = false,
                       data = topHeadLinesState.value.data + it.articles.map {
                           Headline(
                               author = it.author,
                               content = it.content,
                               description = it.description,
                               publishedAt = it.publishedAt,
                               sourceName = it.source.name,
                               title = it.title,
                               url = it.url,
                               imageUrl = it.urlToImage,
                               isBookmarked = false,
                               page = 0
                           )
                       },
                       error = false,
                       reachedMaxHeadlines = it.articles.isEmpty()
                   )
               }
            }.launchIn(viewModelScope)
        }
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