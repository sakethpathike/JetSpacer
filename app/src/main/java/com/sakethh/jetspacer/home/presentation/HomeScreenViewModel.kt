package com.sakethh.jetspacer.home.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.home.domain.model.APODDTO
import com.sakethh.jetspacer.home.domain.useCase.APODApiUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeScreenViewModel(private val apodApiUseCase: APODApiUseCase = APODApiUseCase()) :
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
        apodApiUseCase().onEach {
            when (val apodData = it) {
                is NetworkState.Failure -> {
                    jetSpacerLog(apodData.msg)
                    apodState.value = apodState.value.copy(isLoading = false, error = true)
                }

                is NetworkState.Loading -> {
                    jetSpacerLog("loading")
                    apodState.value = apodState.value.copy(isLoading = true, error = false)
                }

                is NetworkState.Success -> {
                    jetSpacerLog(apodData.data.toString())
                    apodState.value = apodState.value.copy(
                        isLoading = false,
                        error = false,
                        apod = apodData.data
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}