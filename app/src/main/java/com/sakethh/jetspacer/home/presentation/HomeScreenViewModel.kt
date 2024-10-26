package com.sakethh.jetspacer.home.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.home.domain.useCase.HomeScreenRelatedAPIsUseCase
import com.sakethh.jetspacer.home.presentation.state.apod.APODState
import com.sakethh.jetspacer.home.presentation.state.apod.ModifiedAPODDTO
import com.sakethh.jetspacer.home.presentation.state.epic.EPICState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeScreenViewModel(homeScreenRelatedAPIsUseCase: HomeScreenRelatedAPIsUseCase = HomeScreenRelatedAPIsUseCase()) :
    ViewModel() {
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
            )
        )
    )

    val epicState = mutableStateOf(
        EPICState(data = listOf(), isLoading = false, error = false)
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

        homeScreenRelatedAPIsUseCase.epicData(viewModelScope).onEach {
            when (val epicData = it) {
                is NetworkState.Failure -> {
                    jetSpacerLog(epicData.msg)
                }

                is NetworkState.Loading -> {
                    epicState.value = epicState.value.copy(isLoading = true)
                }

                is NetworkState.Success -> {
                    epicState.value = epicState.value.copy(isLoading = false, data = epicData.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}