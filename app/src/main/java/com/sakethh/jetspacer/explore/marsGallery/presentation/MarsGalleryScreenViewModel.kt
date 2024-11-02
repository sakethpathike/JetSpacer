package com.sakethh.jetspacer.explore.marsGallery.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UiChannel
import com.sakethh.jetspacer.explore.marsGallery.domain.model.CameraAndSolSpecificDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.RoverLatestImagesDTO
import com.sakethh.jetspacer.explore.marsGallery.domain.useCase.FetchImagesBasedOnTheFilterUseCase
import com.sakethh.jetspacer.explore.marsGallery.domain.useCase.FetchLatestImagesFromRoverUseCase
import com.sakethh.jetspacer.explore.marsGallery.presentation.state.MarsGalleryCameraSpecificState
import com.sakethh.jetspacer.explore.marsGallery.presentation.state.MarsGalleryLatestImagesState
import com.sakethh.jetspacer.explore.marsGallery.presentation.utils.Rover
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Locale

class MarsGalleryScreenViewModel(
    private val fetchLatestImagesFromRoverUseCase: FetchLatestImagesFromRoverUseCase = FetchLatestImagesFromRoverUseCase(),
    private val fetchImagesBasedOnTheFilterUseCase: FetchImagesBasedOnTheFilterUseCase = FetchImagesBasedOnTheFilterUseCase()
) :
    ViewModel() {
    val latestImagesState = mutableStateOf(
        MarsGalleryLatestImagesState(
            data = RoverLatestImagesDTO(
                latestImages = listOf()
            ), isLoading = true, error = false, roverName = Rover.Curiosity.name,
            statusCode = 0, statusDescription = ""
        )
    )

    val cameraAndSolSpecificState = mutableStateOf(
        MarsGalleryCameraSpecificState(
            isLoading = true, error = false, data = CameraAndSolSpecificDTO(
                photos = listOf()
            ), reachedMaxPages = false,
            statusCode = 0,
            statusDescription = ""
        )
    )

    fun resetCameraAndSolSpecificState() {
        cameraAndSolSpecificState.value = MarsGalleryCameraSpecificState(
            isLoading = true, error = false, data = CameraAndSolSpecificDTO(
                photos = listOf()
            ), reachedMaxPages = false,
            statusCode = 0, statusDescription = ""
        )
    }

    init {
        loadLatestImagesFromRover(Rover.Curiosity.name.lowercase())
    }

    fun loadLatestImagesFromRover(roverName: String) {
        cameraAndSolSpecificState.value = cameraAndSolSpecificState.value.copy(isLoading = false)
        viewModelScope.launch {
            fetchLatestImagesFromRoverUseCase(roverName.lowercase()).collectLatest {
                when (val latestImagesData = it) {
                    is NetworkState.Failure -> {
                        latestImagesState.value =
                            latestImagesState.value.copy(
                                isLoading = false, error = true,
                                roverName = roverName.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                },
                                statusCode = latestImagesData.statusCode,
                                statusDescription = latestImagesData.statusDescription
                            )
                        UiChannel.pushUiEvent(
                            uiEvent = UIEvent.ShowSnackbar(latestImagesData.exceptionMessage),
                            coroutineScope = this
                        )
                    }

                    is NetworkState.Loading -> {
                        latestImagesState.value =
                            latestImagesState.value.copy(
                                isLoading = true, error = false, data = RoverLatestImagesDTO(
                                    latestImages = listOf()
                                ),
                                roverName = roverName.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                }
                            )
                    }

                    is NetworkState.Success -> {
                        latestImagesState.value = latestImagesState.value.copy(
                            isLoading = false,
                            error = false,
                            data = latestImagesData.data,
                            roverName = roverName.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            }
                        )
                    }
                }
            }
        }
    }
    var currentCameraAndSolSpecificPaginatedPage = 0
    fun loadImagesBasedOnTheFilter(
        roverName: String,
        cameraName: String,
        sol: Int,
        page: Int,
        clearData: Boolean = false
    ) {
        latestImagesState.value = latestImagesState.value.copy(isLoading = false)
        fetchImagesBasedOnTheFilterUseCase(roverName, cameraName, sol, page).onEach {
            when (val imagesBasedOnFiltersData = it) {
                is NetworkState.Failure -> {
                    cameraAndSolSpecificState.value = cameraAndSolSpecificState.value.copy(
                        isLoading = false,
                        error = true,
                        statusDescription = imagesBasedOnFiltersData.statusDescription,
                        statusCode = imagesBasedOnFiltersData.statusCode
                    )
                    UiChannel.pushUiEvent(
                        uiEvent = UIEvent.ShowSnackbar(imagesBasedOnFiltersData.exceptionMessage),
                        coroutineScope = this.viewModelScope
                    )
                }

                is NetworkState.Loading -> {
                    cameraAndSolSpecificState.value = cameraAndSolSpecificState.value.copy(
                        isLoading = true,
                        error = false
                    )
                }

                is NetworkState.Success -> {
                    cameraAndSolSpecificState.value = cameraAndSolSpecificState.value.copy(
                        isLoading = false,
                        error = false,
                        data = cameraAndSolSpecificState.value.data.copy(
                            photos = if (clearData) {
                                emptyList()
                            } else {
                                cameraAndSolSpecificState.value.data.photos
                            } + imagesBasedOnFiltersData.data.photos
                        ),
                        reachedMaxPages = imagesBasedOnFiltersData.data.photos.isEmpty()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}