package com.sakethh.jetspacer.home.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.home.domain.model.APODDTO
import com.sakethh.jetspacer.home.domain.useCase.HomeScreenRelatedAPIsUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeScreenViewModel(homeScreenRelatedAPIsUseCase: HomeScreenRelatedAPIsUseCase = HomeScreenRelatedAPIsUseCase()) :
    ViewModel() {
    val apodState = mutableStateOf(
        APODState(
            isLoading = true, error = false, apod = APODDTO(
                copyright = "",
                date = "",
                explanation = "",
                hdUrl = "",
                mediaType = "",
                title = "",
                url = ""
            )
        )
    )

    init {
        homeScreenRelatedAPIsUseCase.apodData().onEach {
            when (val apodData = it) {
                is NetworkState.Failure -> {
                    apodState.value = apodState.value.copy(isLoading = false, error = true)
                }

                is NetworkState.Loading -> {
                    apodState.value = apodState.value.copy(isLoading = true, error = false)
                }

                is NetworkState.Success -> {
                    apodState.value = apodState.value.copy(
                        isLoading = false,
                        error = false,
                        apod = apodData.data
                    )
                }
            }
        }.launchIn(viewModelScope)

        homeScreenRelatedAPIsUseCase.epicData(viewModelScope).onEach {
            when (val epicData = it) {
                is NetworkState.Failure -> {
                    jetSpacerLog(epicData.msg)
                }

                is NetworkState.Loading -> {
                    jetSpacerLog("loading in epic data")
                }

                is NetworkState.Success -> {
                    jetSpacerLog("sucess :" + epicData.data.toString())
                }
            }
        }.launchIn(viewModelScope)
    }
}