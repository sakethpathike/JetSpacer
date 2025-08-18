package com.sakethh.jetspacer.ui.screens.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.onFailure
import com.sakethh.jetspacer.domain.onLoading
import com.sakethh.jetspacer.domain.onSuccess
import com.sakethh.jetspacer.domain.repository.HeadlinesRepository
import com.sakethh.jetspacer.domain.useCase.FetchAPODUseCase
import com.sakethh.jetspacer.domain.useCase.FetchEPICDataUseCase
import com.sakethh.jetspacer.ui.screens.home.state.HeadlinesState
import com.sakethh.jetspacer.ui.screens.home.state.apod.APODState
import com.sakethh.jetspacer.ui.screens.home.state.apod.ModifiedAPODDTO
import com.sakethh.jetspacer.ui.screens.home.state.epic.EPICState
import com.sakethh.jetspacer.ui.utils.UIChannel
import com.sakethh.jetspacer.ui.utils.pushUIEvent
import com.sakethh.jetspacer.ui.utils.retrievePaletteFromUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    context: Context,
    private val headlinesRepository: HeadlinesRepository,
    fetchAPODUseCase: FetchAPODUseCase,
    fetchEPICDataUseCase: FetchEPICDataUseCase
) : ViewModel() {

    val topHeadLinesState = mutableStateOf(
        HeadlinesState(
            isLoading = true,
            data = emptyList(),
            error = false,
            reachedMaxHeadlines = false,
            statusCode = 0,
            statusDescription = ""
        )
    )


    companion object {
        var currentAPODImgURL by mutableStateOf("")
            private set
    }

    private var currentPage = 0
    private var newsAPIJob: Job? = null
    fun retrievePaginatedTopHeadlines(context: Context) {
        newsAPIJob?.cancel()
        topHeadLinesState.value = topHeadLinesState.value.copy(
            isLoading = true, error = false, reachedMaxHeadlines = false
        )
        viewModelScope.launch {
            newsAPIJob = headlinesRepository.getTopHeadLines(pageSize = 10, page = ++currentPage)
                .cancellable().onEach {
                    it.onFailure {
                        topHeadLinesState.value = topHeadLinesState.value.copy(
                            isLoading = false,
                            error = true,
                            statusCode = it.statusCode,
                            statusDescription = it.statusDescription,
                            reachedMaxHeadlines = false
                        )
                        pushUIEvent(UIChannel.Type.ShowSnackbar(it.exceptionMessage))
                    }.onSuccess {
                        val newArticles = it.articles

                        if (newArticles.isEmpty()) {
                            topHeadLinesState.value = topHeadLinesState.value.copy(
                                isLoading = false, reachedMaxHeadlines = true, error = false
                            )
                            return@onSuccess
                        }

                        val existingArticlesCount = topHeadLinesState.value.data.size

                        topHeadLinesState.value = topHeadLinesState.value.copy(
                            isLoading = false,
                            data = topHeadLinesState.value.data + newArticles,
                            error = false,
                        )

                        newArticles.indices.asFlow().flatMapMerge { index ->
                            flow {
                                val palette =
                                    if (newArticles[index].urlToImage.endsWith(".gif")) null else retrievePaletteFromUrl(
                                        context, newArticles[index].urlToImage
                                    )
                                val colors = if (palette == null) {
                                    emptyList()
                                } else {
                                    buildList {
                                        palette.vibrantSwatch?.rgb?.let { add(Color(it)) }
                                        palette.lightVibrantSwatch?.rgb?.let { add(Color(it)) }
                                        palette.mutedSwatch?.rgb?.let { add(Color(it)) }
                                        palette.darkMutedSwatch?.rgb?.let { add(Color(it)) }
                                        add(Color.Transparent)
                                    }
                                }
                                emit(index + existingArticlesCount to colors)
                            }
                        }.flowOn(Dispatchers.IO).collect { (index, colors) ->
                            topHeadLinesState.value.colors[index] = colors
                        }
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
            ) to emptyList(), statusCode = 0, statusDescription = ""
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
        retrievePaginatedTopHeadlines(context)
        viewModelScope.launch {
            fetchAPODUseCase().onEach {
                when (val apodData = it) {
                    is Response.Failure -> {
                        apodState.value = apodState.value.copy(
                            isLoading = false,
                            error = true,
                            statusCode = apodData.statusCode,
                            statusDescription = apodData.statusDescription
                        )
                        pushUIEvent(UIChannel.Type.ShowSnackbar(apodData.exceptionMessage))
                    }

                    is Response.Loading -> {
                        apodState.value = apodState.value.copy(isLoading = true, error = false)
                    }

                    is Response.Success -> {
                        currentAPODImgURL = apodData.data.url ?: ""
                        apodState.value = apodState.value.copy(
                            isLoading = false, error = false, apod = ModifiedAPODDTO(
                                copyright = apodData.data.copyright ?: "",
                                date = apodData.data.date ?: "",
                                explanation = apodData.data.explanation ?: "",
                                hdUrl = apodData.data.hdUrl ?: "",
                                mediaType = apodData.data.mediaType ?: "",
                                title = apodData.data.title ?: "",
                                url = apodData.data.url ?: ""
                            ) to run {
                                if (apodData.data.url == null) return@run emptyList()
                                val palette = retrievePaletteFromUrl(context, apodData.data.url)
                                if (palette == null) {
                                    emptyList()
                                } else {
                                    buildList {
                                        add(Color.Transparent)
                                        add(Color.Transparent)
                                        add(Color.Transparent)
                                        palette.vibrantSwatch?.rgb?.let { add(Color(it)) }
                                        palette.lightVibrantSwatch?.rgb?.let { add(Color(it)) }
                                        palette.mutedSwatch?.rgb?.let { add(Color(it)) }
                                        palette.darkMutedSwatch?.rgb?.let { add(Color(it)) }
                                        add(Color.Transparent)
                                    }
                                }
                            })
                    }
                }
            }.launchIn(viewModelScope)
        }

        viewModelScope.launch {
            fetchEPICDataUseCase().onEach {
                when (val epicData = it) {
                    is Response.Failure -> {
                        epicState = epicState.copy(
                            isLoading = false,
                            error = true,
                            statusCode = epicData.statusCode,
                            statusDescription = epicData.statusDescription
                        )
                        pushUIEvent(UIChannel.Type.ShowSnackbar(epicData.exceptionMessage))
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
}