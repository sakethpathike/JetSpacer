package com.sakethh.jetspacer.ui.screens.explore.marsGallery

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.Rover
import com.sakethh.jetspacer.domain.model.CameraAndSolSpecificDTO
import com.sakethh.jetspacer.domain.model.rover_latest_images.RoverLatestImagesDTO
import com.sakethh.jetspacer.domain.useCase.FetchLatestImagesFromRoverUseCase
import com.sakethh.jetspacer.domain.useCase.FetchRoverImagesBasedOnTheFilterUseCase
import com.sakethh.jetspacer.ui.screens.explore.marsGallery.state.MarsGalleryCameraSpecificState
import com.sakethh.jetspacer.ui.screens.explore.marsGallery.state.MarsGalleryLatestImagesState
import com.sakethh.jetspacer.ui.utils.UIChannel
import com.sakethh.jetspacer.ui.utils.pushUIEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Locale

class MarsGalleryScreenViewModel(
    private val fetchLatestImagesFromRoverUseCase: FetchLatestImagesFromRoverUseCase,
    private val fetchRoverImagesBasedOnTheFilterUseCase: FetchRoverImagesBasedOnTheFilterUseCase
) : ViewModel() {
    val latestImagesState = mutableStateOf(
        MarsGalleryLatestImagesState(
            data = RoverLatestImagesDTO(
                latestImages = listOf()
            ),
            isLoading = true,
            error = false,
            roverName = Rover.Curiosity.name,
            statusCode = 0,
            statusDescription = ""
        )
    )

    val cameraAndSolSpecificState = mutableStateOf(
        MarsGalleryCameraSpecificState(
            isLoading = true, error = false, data = CameraAndSolSpecificDTO(
                photos = listOf()
            ), reachedMaxPages = false, statusCode = 0, statusDescription = ""
        )
    )

    fun resetCameraAndSolSpecificState() {
        cameraAndSolSpecificState.value = MarsGalleryCameraSpecificState(
            isLoading = true, error = false, data = CameraAndSolSpecificDTO(
                photos = listOf()
            ), reachedMaxPages = false, statusCode = 0, statusDescription = ""
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
                    is Response.Failure -> {
                        latestImagesState.value = latestImagesState.value.copy(
                            isLoading = false,
                            error = true,
                            roverName = roverName.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            },
                            statusCode = latestImagesData.statusCode,
                            statusDescription = latestImagesData.statusDescription
                        )
                        pushUIEvent(UIChannel.Type.ShowSnackbar(latestImagesData.exceptionMessage))
                    }

                    is Response.Loading -> {
                        latestImagesState.value = latestImagesState.value.copy(
                            isLoading = true, error = false, data = RoverLatestImagesDTO(
                                latestImages = listOf()
                            ), roverName = roverName.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            })
                    }

                    is Response.Success -> {
                        latestImagesState.value = latestImagesState.value.copy(
                            isLoading = false,
                            error = false,
                            data = latestImagesData.data,
                            roverName = roverName.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            })
                    }
                }
            }
        }
    }

    var currentCameraAndSolSpecificPaginatedPage = 0
    fun loadImagesBasedOnTheFilter(
        roverName: String, cameraName: String, sol: Int, page: Int, clearData: Boolean = false
    ) {
        latestImagesState.value = latestImagesState.value.copy(isLoading = false)
        viewModelScope.launch {
            fetchRoverImagesBasedOnTheFilterUseCase(roverName, cameraName, sol, page).onEach {
                when (val imagesBasedOnFiltersData = it) {
                    is Response.Failure -> {
                        cameraAndSolSpecificState.value = cameraAndSolSpecificState.value.copy(
                            isLoading = false,
                            error = true,
                            statusDescription = imagesBasedOnFiltersData.statusDescription,
                            statusCode = imagesBasedOnFiltersData.statusCode
                        )
                        pushUIEvent(UIChannel.Type.ShowSnackbar(imagesBasedOnFiltersData.exceptionMessage))
                    }

                    is Response.Loading -> {
                        cameraAndSolSpecificState.value = cameraAndSolSpecificState.value.copy(
                            isLoading = true, error = false
                        )
                    }

                    is Response.Success -> {
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
}