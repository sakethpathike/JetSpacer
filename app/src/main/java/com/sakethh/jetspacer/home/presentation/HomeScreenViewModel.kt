package com.sakethh.jetspacer.home.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UiChannel
import com.sakethh.jetspacer.home.domain.useCase.FetchCurrentAPODUseCase
import com.sakethh.jetspacer.home.domain.useCase.FetchCurrentEPICDataUseCase
import com.sakethh.jetspacer.home.presentation.state.apod.APODState
import com.sakethh.jetspacer.home.presentation.state.apod.ModifiedAPODDTO
import com.sakethh.jetspacer.home.presentation.state.epic.EPICState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeScreenViewModel(
    fetchCurrentAPODUseCase: FetchCurrentAPODUseCase = FetchCurrentAPODUseCase(),
    fetchCurrentEPICDataUseCase: FetchCurrentEPICDataUseCase = FetchCurrentEPICDataUseCase()
) :
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
            ),
            statusCode = 0,
            statusDescription = ""
        )
    )

    val epicState = mutableStateOf(
        EPICState(
            data = listOf(),
            isLoading = false,
            error = false,
            statusCode = 0,
            statusDescription = ""
        )
    )

    init {
        fetchCurrentAPODUseCase().onEach {
            when (val apodData = it) {
                is NetworkState.Failure -> {
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

        fetchCurrentEPICDataUseCase().onEach {
            when (val epicData = it) {
                is NetworkState.Failure -> {
                    epicState.value = epicState.value.copy(
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