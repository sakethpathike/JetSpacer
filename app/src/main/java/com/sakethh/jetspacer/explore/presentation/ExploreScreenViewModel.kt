package com.sakethh.jetspacer.explore.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.explore.domain.model.api.iss.modified.ISSLocationModifiedDTO
import com.sakethh.jetspacer.explore.domain.useCase.ExploreScreenRelatedAPISUseCase
import com.sakethh.jetspacer.explore.presentation.state.SearchResultState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Suppress("OPT_IN_USAGE")
class ExploreScreenViewModel(private val exploreScreenRelatedAPISUseCase: ExploreScreenRelatedAPISUseCase = ExploreScreenRelatedAPISUseCase()) :
    ViewModel() {

    val searchResultsState = mutableStateOf(
        SearchResultState(
            emptyList(),
            isLoading = false,
            error = false
        )
    )

    val issLocationState = mutableStateOf(
        ISSLocationModifiedDTO(latitude = "", longitude = "", message = "", timestamp = "")
    )

    companion object {
        val isSearchBarExpanded = mutableStateOf(false)
    }

    val querySearch = mutableStateOf("")
    private var job: Job? = null

    init {
        snapshotFlow { querySearch.value }.onEach { query ->
            job?.cancel()
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

        viewModelScope.launch {
            while (true) {
                when (val issLocationData = exploreScreenRelatedAPISUseCase.issLocation()) {
                    is NetworkState.Success -> {
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

    private fun loadSearchResults(querySearchSnapShotFlow: Flow<String>) {
        job = viewModelScope.launch {
            querySearchSnapShotFlow.cancellable().collectLatest {
                exploreScreenRelatedAPISUseCase.nasaImageLibrarySearch(it, 1).onEach {
                    when (val nasaSearchData = it) {
                        is NetworkState.Failure -> {
                            searchResultsState.value =
                                searchResultsState.value.copy(isLoading = false, error = true)
                        }

                        is NetworkState.Loading -> {
                            searchResultsState.value =
                                searchResultsState.value.copy(isLoading = true, error = false)
                        }

                        is NetworkState.Success -> {
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