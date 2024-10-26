package com.sakethh.jetspacer.explore.apodArchive.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.explore.apodArchive.domain.useCase.APODArchiveUseCase
import com.sakethh.jetspacer.home.presentation.state.apod.ModifiedAPODDTO
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class APODArchiveScreenViewModel(private val apodArchiveUseCase: APODArchiveUseCase = APODArchiveUseCase()) :
    ViewModel() {
    val apodArchiveState = mutableStateOf(
        APODArchiveScreenState(
            data = emptyList(),
            isLoading = false,
            error = false
        )
    )

    private val dateFormat = SimpleDateFormat("yyyy-M-dd")
    private var startDate = dateFormat.format(Date())
    private var endDate = Calendar.getInstance()

    init {
        retrieveNextBatchOfAPODArchive()
    }

    fun retrieveNextBatchOfAPODArchive() {
        endDate.time = dateFormat.parse(startDate)
        endDate.add(Calendar.DAY_OF_YEAR, -30)
        val modifiedEndDate = dateFormat.format(endDate.time)

        apodArchiveUseCase(startDate, modifiedEndDate).cancellable().onEach {
            when (val apodData = it) {
                is NetworkState.Failure -> {
                    jetSpacerLog(apodData.msg)
                }

                is NetworkState.Loading -> {

                }

                is NetworkState.Success -> {
                    apodArchiveState.value = apodArchiveState.value.copy(data = apodData.data.map {
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
}