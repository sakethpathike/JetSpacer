package com.sakethh.jetspacer.explore.apodArchive.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UiChannel
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.explore.apodArchive.domain.useCase.APODArchiveUseCase
import com.sakethh.jetspacer.home.domain.useCase.HomeScreenRelatedAPIsUseCase
import com.sakethh.jetspacer.home.presentation.state.apod.ModifiedAPODDTO
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Calendar

class APODArchiveScreenViewModel(
    private val apodArchiveUseCase: APODArchiveUseCase = APODArchiveUseCase(),
    private val homeScreenRelatedAPIsUseCase: HomeScreenRelatedAPIsUseCase = HomeScreenRelatedAPIsUseCase()
) :
    ViewModel() {
    val apodArchiveState = mutableStateOf(
        APODArchiveScreenState(
            data = emptyList(),
            isLoading = false,
            error = false,
            statusDescription = "",
            statusCode = 0
        )
    )

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    private var startDate = ""

    private var endDate = Calendar.getInstance()
    var apodStartDate = ""

    init {
        homeScreenRelatedAPIsUseCase.apodData().onEach {
            when (val apodData = it) {
                is NetworkState.Failure -> {
                    apodArchiveState.value = apodArchiveState.value.copy(
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
                    apodArchiveState.value = apodArchiveState.value.copy(isLoading = true)
                }

                is NetworkState.Success -> {
                    apodData.data.date?.let {
                        apodStartDate = it
                        startDate = it
                        retrieveNextBatchOfAPODArchive()
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun resetAPODArchiveStateAndReloadAgain() {
        startDate = apodStartDate
        apodArchiveState.value =
            apodArchiveState.value.copy(isLoading = true, data = emptyList(), error = false)
        retrieveNextBatchOfAPODArchive()
    }

    fun retrieveAPODDataBetweenSpecificDates(
        apodStartDate: String,
        apodEndDate: String,
        erasePreviousData: Boolean = false
    ) {
        if (erasePreviousData) {
            apodArchiveState.value = apodArchiveState.value.copy(data = emptyList())
        }

        apodArchiveUseCase(apodStartDate, apodEndDate).cancellable().onEach {
            when (val apodData = it) {
                is NetworkState.Failure -> {
                    apodArchiveState.value =
                        apodArchiveState.value.copy(
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
                    apodArchiveState.value =
                        apodArchiveState.value.copy(isLoading = true, error = false)
                }

                is NetworkState.Success -> {
                    apodArchiveState.value = apodArchiveState.value.copy(
                        isLoading = false,
                        error = false,
                        data = apodArchiveState.value.data + apodData.data.map {
                            ModifiedAPODDTO(
                                copyright = it.copyright ?: "",
                                date = it.date ?: "",
                                explanation = it.explanation ?: "",
                                hdUrl = it.hdUrl ?: "",
                                mediaType = it.mediaType ?: "",
                                title = it.title ?: "",
                                url = it.url ?: ""
                            )
                        })
                }
            }
        }.launchIn(viewModelScope)
    }

    fun retrieveNextBatchOfAPODArchive() {

        jetSpacerLog("start date : $startDate")

        endDate.time = dateFormat.parse(startDate)
        endDate.add(Calendar.DAY_OF_YEAR, -30)
        val modifiedEndDate = dateFormat.format(endDate.time)

        jetSpacerLog("end date : $modifiedEndDate")

        retrieveAPODDataBetweenSpecificDates(startDate, modifiedEndDate)

        startDate = dateFormat.format(
            Calendar.getInstance().apply {
                time = dateFormat.parse(modifiedEndDate)
                add(Calendar.DAY_OF_YEAR, -1)
            }.time
        )
    }
}