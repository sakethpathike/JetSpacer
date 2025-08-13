package com.sakethh.jetspacer.ui.screens.explore

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.utils.logger
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.iss.modified.ISSLocationModifiedDTO
import com.sakethh.jetspacer.domain.useCase.FetchISSLocationUseCase
import com.sakethh.jetspacer.domain.useCase.FetchImagesFromNasaImageLibraryUseCase
import com.sakethh.jetspacer.ui.screens.explore.search.state.SearchResultState
import com.sakethh.jetspacer.ui.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.ui.utils.generateColorPaletteList
import com.sakethh.jetspacer.ui.utils.retrievePaletteFromUrl
import com.sakethh.jetspacer.ui.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.ui.utils.uiEvent.UiChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Suppress("OPT_IN_USAGE")
class ExploreScreenViewModel(
    context: Context,
    private val fetchImagesFromNasaImageLibraryUseCase: FetchImagesFromNasaImageLibraryUseCase = FetchImagesFromNasaImageLibraryUseCase(),
    private val fetchISSLocationUseCase: FetchISSLocationUseCase = FetchISSLocationUseCase()
) : ViewModel() {

    val searchResultsState = mutableStateOf(
        SearchResultState(
            data = emptyList(),
            isLoading = false,
            error = false, statusCode = 0, statusDescription = "",
        )
    )

    val issLocationState = mutableStateOf(
        ISSLocationModifiedDTO(
            latitude = "",
            longitude = "",
            message = "",
            timestamp = "",
            error = false,
            errorMessage = ""
        )
    )

    val apodArchiveBannerColors = mutableStateListOf<Color>()
    val marsGalleryBannerColors = mutableStateListOf<Color>()

    companion object {
        val isSearchBarExpanded = mutableStateOf(false)
    }

    val querySearch = mutableStateOf("")
    private var searchResultsJob: Job? = null
    private var issLocationJob: Job? = null

    init {
        viewModelScope.launch {
            retrievePaletteFromUrl(
                context = context,
                url = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Curiosity_Self-Portrait_at_%27Big_Sky%27_Drilling_Site.jpg/435px-Curiosity_Self-Portrait_at_%27Big_Sky%27_Drilling_Site.jpg"
            )?.let {
                marsGalleryBannerColors.addAll(generateColorPaletteList(it))
            }
        }

        viewModelScope.launch {
            snapshotFlow {
                HomeScreenViewModel.currentAPODImgURL
            }.collectLatest {
                retrievePaletteFromUrl(
                    context = context, url = it.ifBlank {
                        "https://apod.nasa.gov/apod/image/2410/IC63_1024.jpg"
                    })?.let {
                    apodArchiveBannerColors.addAll(generateColorPaletteList(it))
                }
            }
        }

        snapshotFlow { querySearch.value }.onEach { query ->
            searchResultsJob?.cancel()
            if (query.isBlank()) {
                searchResultsState.value = searchResultsState.value.copy(
                    isLoading = false, error = false, data = emptyList()
                )
                return@onEach
            }
            searchResultsState.value =
                searchResultsState.value.copy(isLoading = true, error = false, data = emptyList())
        }.debounce(1500).onEach { query ->
            loadSearchResults(context, flowOf(query))
        }.launchIn(viewModelScope)

        issLocationJob = viewModelScope.launch {
            while (isActive) {
                logger("retrieving issLocation")
                when (val issLocationData = fetchISSLocationUseCase()) {
                    is Response.Success -> {
                        issLocationState.value = issLocationData.data.copy(error = false)
                    }

                    is Response.Failure<*> -> {
                        cancel()
                        issLocationState.value = issLocationState.value.copy(
                            error = true,
                            errorMessage = "${issLocationData.statusCode}\n${issLocationData.statusDescription}"
                        )
                    }

                    is Response.Loading<*> -> {}
                }
                delay(5000)
            }
        }
    }

    private fun loadSearchResults(context: Context, querySearchSnapShotFlow: Flow<String>) {
        searchResultsJob = viewModelScope.launch {
            querySearchSnapShotFlow.cancellable().collectLatest {
                if (it.isBlank()) {
                    return@collectLatest
                }
                fetchImagesFromNasaImageLibraryUseCase(it, 1).onEach {
                    when (val nasaSearchData = it) {
                        is Response.Failure -> {
                            searchResultsState.value = searchResultsState.value.copy(
                                isLoading = false,
                                error = true,
                                statusCode = nasaSearchData.statusCode,
                                statusDescription = nasaSearchData.statusDescription
                            )
                            UiChannel.pushUiEvent(
                                uiEvent = UIEvent.ShowSnackbar(nasaSearchData.exceptionMessage),
                                coroutineScope = this
                            )
                        }

                        is Response.Loading -> {
                            searchResultsState.value =
                                searchResultsState.value.copy(isLoading = true, error = false)
                        }

                        is Response.Success -> {
                            searchResultsState.value = searchResultsState.value.copy(
                                isLoading = false, error = false, data = nasaSearchData.data.map {
                                    it to run {
                                        val palette = retrievePaletteFromUrl(
                                            context = context, url = it.imageUrl
                                        )
                                        if (palette == null) {
                                            emptyList()
                                        } else {
                                            generateColorPaletteList(palette)
                                        }
                                    }
                                })
                        }
                    }
                }.launchIn(this)
            }
        }
    }
}