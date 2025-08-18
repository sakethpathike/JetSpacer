package com.sakethh.jetspacer.ui.screens.explore

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.sakethh.jetspacer.core.common.utils.logger
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.useCase.FetchISSLocationUseCase
import com.sakethh.jetspacer.domain.useCase.FetchImagesFromNasaImageLibraryUseCase
import com.sakethh.jetspacer.ui.navigation.HyleNavigation
import com.sakethh.jetspacer.ui.screens.explore.search.state.SearchResultState
import com.sakethh.jetspacer.ui.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.ui.utils.UIChannel
import com.sakethh.jetspacer.ui.utils.generateColorPaletteList
import com.sakethh.jetspacer.ui.utils.pushUIEvent
import com.sakethh.jetspacer.ui.utils.retrievePaletteFromUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Suppress("OPT_IN_USAGE")
class ExploreScreenViewModel(
    navController: NavController,
    context: Context,
    private val fetchImagesFromNasaImageLibraryUseCase: FetchImagesFromNasaImageLibraryUseCase,
    private val fetchISSLocationUseCase: FetchISSLocationUseCase
) : ViewModel() {

    val searchResultsState = mutableStateOf(
        SearchResultState(
            data = emptyList(),
            isLoading = false,
            error = false, statusCode = 0, statusDescription = "",
        )
    )

    val issLocationState = mutableStateOf(
        ISSLocationState(
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
            navController.currentBackStackEntryFlow.collectLatest {
                if (it.destination.hasRoute(HyleNavigation.Root.Explore::class)) {
                    trackISSLocation()
                    logger("trackISSLocation()")
                } else {
                    logger("stopTrackingISSLocation()")
                    stopTrackingISSLocation()
                }
            }
        }
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
    }

    private fun stopTrackingISSLocation() = issLocationJob?.cancel()

    private fun trackISSLocation() {
        issLocationJob?.cancel()
        issLocationJob = viewModelScope.launch {
            while (isActive) {
                logger("retrieving issLocation")
                fetchISSLocationUseCase().collectLatest {
                    when (val issLocationResponse = it) {
                        is Response.Success -> {
                            issLocationState.value = issLocationResponse.data.copy(error = false)
                        }

                        is Response.Failure<*> -> {
                            cancel()
                            issLocationState.value = issLocationState.value.copy(
                                error = true,
                                errorMessage = "${issLocationResponse.statusCode}\n${issLocationResponse.statusDescription}"
                            )
                        }

                        is Response.Loading<*> -> {}
                    }
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
                            pushUIEvent(UIChannel.Type.ShowSnackbar(nasaSearchData.exceptionMessage))
                        }

                        is Response.Loading -> {
                            searchResultsState.value =
                                searchResultsState.value.copy(isLoading = true, error = false)
                        }

                        is Response.Success -> {
                            searchResultsState.value = searchResultsState.value.copy(
                                isLoading = false,
                                error = false,
                                data = nasaSearchData.data,
                                colors = mutableStateMapOf()
                            )

                            // this runs 16 retrievals in parallel at a time by default
                            // > Default concurrency limit that is used by flattenMerge and flatMapMerge operators. It is 16 by default and can be changed on JVM using DEFAULT_CONCURRENCY_PROPERTY_NAME property.
                            nasaSearchData.data.indices.asFlow().flatMapMerge { index ->
                                flow {
                                    val palette = retrievePaletteFromUrl(
                                        context = context, url = nasaSearchData.data[index].imageUrl
                                    )
                                    val colors = if (palette == null) {
                                        emptyList()
                                    } else {
                                        generateColorPaletteList(palette)
                                    }
                                    emit(index to colors)
                                }
                            }.flowOn(Dispatchers.IO).collect { (index, colors) ->
                                searchResultsState.value.colors[index] = colors
                            }
                        }
                    }
                }.launchIn(this)
            }
        }
    }
}