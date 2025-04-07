package com.sakethh.jetspacer.ui.screens.explore

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.utils.logger
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.iss.modified.ISSLocationModifiedDTO
import com.sakethh.jetspacer.domain.useCase.FetchISSLocationUseCase
import com.sakethh.jetspacer.domain.useCase.FetchImagesFromNasaImageLibraryUseCase
import com.sakethh.jetspacer.ui.screens.explore.search.state.SearchResultState
import com.sakethh.jetspacer.ui.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.ui.utils.uiEvent.UiChannel
import kotlinx.coroutines.Job
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
    private val fetchImagesFromNasaImageLibraryUseCase: FetchImagesFromNasaImageLibraryUseCase = FetchImagesFromNasaImageLibraryUseCase(),
    private val fetchISSLocationUseCase: FetchISSLocationUseCase = FetchISSLocationUseCase()
) :
    ViewModel() {

    val searchResultsState = mutableStateOf(
        SearchResultState(
            data = emptyList(),
            isLoading = false,
            error = false, statusCode = 0, statusDescription = "",
        )
    )

    val issLocationState = mutableStateOf(
        ISSLocationModifiedDTO(latitude = "", longitude = "", message = "", timestamp = "")
    )

    companion object {
        val isSearchBarExpanded = mutableStateOf(false)
    }

    val querySearch = mutableStateOf("")
    private var searchResultsJob: Job? = null
    private var issLocationJob: Job? = null

    init {
        snapshotFlow { querySearch.value }.onEach { query ->
            searchResultsJob?.cancel()
            if (query.isBlank()) {
                searchResultsState.value = searchResultsState.value.copy(
                    isLoading = false,
                    error = false,
                    data = emptyList()
                )
                return@onEach
            }
            searchResultsState.value =
                searchResultsState.value.copy(isLoading = true, error = false, data = emptyList())
        }.debounce(1500).onEach { query ->
            loadSearchResults(flowOf(query))
        }.launchIn(viewModelScope)

        issLocationJob = viewModelScope.launch {
            while (isActive) {
                logger("retrieving issLocation")
                when (val issLocationData = fetchISSLocationUseCase()) {
                    is Response.Success -> {
                        issLocationState.value = issLocationData.data
                    }

                    else -> {
                        issLocationState.value = ISSLocationModifiedDTO(
                            latitude = "",
                            longitude = "",
                            message = "",
                            timestamp = ""
                        )
                    }
                }
                delay(5000)
            }
        }
    }

    fun stopIssLocationRetrieval() {
        issLocationJob?.cancel()
    }

    private fun loadSearchResults(querySearchSnapShotFlow: Flow<String>) {
        searchResultsJob = viewModelScope.launch {
            querySearchSnapShotFlow.cancellable().collectLatest {
                if (it.isBlank()) {
                    return@collectLatest
                }
                fetchImagesFromNasaImageLibraryUseCase(it, 1).onEach {
                    when (val nasaSearchData = it) {
                        is Response.Failure -> {
                            searchResultsState.value =
                                searchResultsState.value.copy(
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
                                isLoading = false, error = false, data = nasaSearchData.data
                            )
                        }
                    }
                }.launchIn(this)
            }
        }
    }
}