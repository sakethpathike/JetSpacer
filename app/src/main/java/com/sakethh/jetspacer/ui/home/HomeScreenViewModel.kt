package com.sakethh.jetspacer.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.useCase.FetchCurrentAPODUseCase
import com.sakethh.jetspacer.domain.useCase.FetchCurrentEPICDataUseCase
import com.sakethh.jetspacer.ui.home.state.apod.APODState
import com.sakethh.jetspacer.ui.home.state.apod.ModifiedAPODDTO
import com.sakethh.jetspacer.ui.home.state.epic.EPICState
import com.sakethh.jetspacer.ui.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.ui.utils.uiEvent.UiChannel
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
                is Response.Failure -> {
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

                is Response.Loading -> {
                    epicState.value = epicState.value.copy(isLoading = true)
                }

                is Response.Success -> {
                    epicState.value = epicState.value.copy(isLoading = false, data = epicData.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}