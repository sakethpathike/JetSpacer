package com.sakethh.jetspacer.ui.screens.home

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
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
    context: Context,
    private val topHeadlinesRepository: TopHeadlinesDataRepository = TopHeadlinesDataImplementation(
        Network.ktorClient, topHeadlinesDao = HyleApplication.getLocalDb().topHeadlinesDao
    ),
    fetchCurrentAPODUseCase: FetchCurrentAPODUseCase = FetchCurrentAPODUseCase(),
    fetchCurrentEPICDataUseCase: FetchCurrentEPICDataUseCase = FetchCurrentEPICDataUseCase()
) : ViewModel() {

    private val headlines = mutableStateListOf<Pair<Headline, List<Color>>>()

    val topHeadLinesState = mutableStateOf(
        NewsScreenState(
            isLoading = true,
            data = headlines,
            error = false,
            reachedMaxHeadlines = false,
            statusCode = 0,
            statusDescription = ""
        )
    )


    private var currentPage = 0
    private var newsAPIJob: Job? = null
    fun retrievePaginatedTopHeadlines(context: Context) {
        newsAPIJob?.cancel()

        viewModelScope.launch {
            newsAPIJob =
                topHeadlinesRepository.getTopHeadLinesFromRemoteAPI(10, ++currentPage).cancellable()
                    .onEach {
                        it.onLoading {
                            topHeadLinesState.value =
                                topHeadLinesState.value.copy(isLoading = true, error = false)
                        }.onFailure {
                            topHeadLinesState.value = topHeadLinesState.value.copy(
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
                                    val palette = fetchSwatchesFromUrl(context, it.urlToImage)
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
                                    ) to if (palette == null) {
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
                                },
                                error = false,
                                reachedMaxHeadlines = it.articles.isEmpty()
                            )
                        }
                    }.launchIn(viewModelScope)
        }
    }

    suspend fun fetchSwatchesFromUrl(context: Context, url: String): Palette? {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context).data(url).allowHardware(false).build()

        val result = loader.execute(request)
        val bitmap = (result.drawable as? BitmapDrawable)?.bitmap
        return if (bitmap == null) null else Palette.from(bitmap).generate()
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
                            val palette = fetchSwatchesFromUrl(context, apodData.data.url)
                            if (palette == null) {
                                emptyList()
                            } else {
                                buildList {
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