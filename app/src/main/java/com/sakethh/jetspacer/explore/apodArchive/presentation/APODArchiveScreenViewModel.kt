package com.sakethh.jetspacer.explore.apodArchive.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
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
            error = false
        )
    )

    private val dateFormat = SimpleDateFormat("yyyy-M-dd")

    private var startDate = ""

    private var endDate = Calendar.getInstance()

    init {
        homeScreenRelatedAPIsUseCase.apodData().onEach {
            when (val apodData = it) {
                is NetworkState.Failure -> {

                }

                is NetworkState.Loading -> {

                }

                is NetworkState.Success -> {
                    apodData.data.date?.let {
                        startDate = it
                        retrieveNextBatchOfAPODArchive()
                    }
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

        apodArchiveUseCase(startDate, modifiedEndDate).cancellable().onEach {
            when (val apodData = it) {
                is NetworkState.Failure -> {
                    apodArchiveState.value =
                        apodArchiveState.value.copy(isLoading = false, error = true)
                    jetSpacerLog("error")
                }

                is NetworkState.Loading -> {
                    apodArchiveState.value =
                        apodArchiveState.value.copy(isLoading = true, error = false)
                    jetSpacerLog("loading")
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

        startDate = dateFormat.format(
            Calendar.getInstance().apply {
                time = dateFormat.parse(modifiedEndDate)
                add(Calendar.DAY_OF_YEAR, -1)
            }.time
        )
    }
}