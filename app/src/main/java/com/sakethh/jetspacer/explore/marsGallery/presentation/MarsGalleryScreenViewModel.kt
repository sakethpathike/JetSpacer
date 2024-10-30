package com.sakethh.jetspacer.explore.marsGallery.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.RoverLatestImagesDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.useCase.MarsGalleryUseCase
import com.sakethh.jetspacer.explore.marsGallery.presentation.state.latest.MarsGalleryLatestImagesState
import com.sakethh.jetspacer.explore.marsGallery.presentation.utils.Rover
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MarsGalleryScreenViewModel(private val marsGalleryUseCase: MarsGalleryUseCase = MarsGalleryUseCase()) :
    ViewModel() {
    val latestImagesState = mutableStateOf(
        MarsGalleryLatestImagesState(
            data = RoverLatestImagesDTO(
                latestImages = listOf()
            ), isLoading = true, error = false
        )
    )

    init {
        loadLatestImagesFromRover(Rover.Curiosity.name.lowercase())
    }

    fun loadLatestImagesFromRover(roverName: String) {
        viewModelScope.launch {
            marsGalleryUseCase(roverName).collectLatest {
                when (val latestImagesData = it) {
                    is NetworkState.Failure -> {
                        latestImagesState.value =
                            latestImagesState.value.copy(isLoading = false, error = true)
                    }

                    is NetworkState.Loading -> {
                        latestImagesState.value =
                            latestImagesState.value.copy(isLoading = true, error = false)
                    }

                    is NetworkState.Success -> {
                        latestImagesState.value = latestImagesState.value.copy(
                            isLoading = false,
                            error = false,
                            data = latestImagesData.data
                        )
                    }
                }
            }
        }
    }
}