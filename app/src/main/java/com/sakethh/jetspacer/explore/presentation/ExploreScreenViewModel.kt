package com.sakethh.jetspacer.explore.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.explore.domain.useCase.NASAImageLibrarySearchUseCase
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("OPT_IN_USAGE")
class ExploreScreenViewModel(private val nasaImageLibrarySearchUseCase: NASAImageLibrarySearchUseCase = NASAImageLibrarySearchUseCase()) :
    ViewModel() {

    companion object {
        val isSearchBarExpanded = mutableStateOf(false)
    }

    val querySearch = mutableStateOf("")

    init {
        snapshotFlow { querySearch.value }.debounce(1500).onEach {
            jetSpacerLog(it)
            nasaImageLibrarySearchUseCase(it, 1).onEach {
                when (val nasaSearchData = it) {
                    is NetworkState.Failure -> {
                        jetSpacerLog(nasaSearchData.msg)
                    }

                    is NetworkState.Loading -> jetSpacerLog("Loading")
                    is NetworkState.Success -> jetSpacerLog(nasaSearchData.data.toString())
                }
            }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }
}