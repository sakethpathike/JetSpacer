package com.sakethh.jetspacer.ui.screens.explore.marsGallery

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.data.repository.LocalRoverImagesImplementation
import com.sakethh.jetspacer.domain.model.rover.RoverImage
import com.sakethh.jetspacer.domain.repository.LocalRoverImagesRepository
import kotlinx.coroutines.launch

class RoverImageDetailsBtmSheetViewModel(
    private val localRoverImagesRepository: LocalRoverImagesRepository = LocalRoverImagesImplementation()
) : ViewModel() {
    val doesImageExistsInLocalDB = mutableStateOf(false)

    fun doesThisImageExists(imgUrl: String) {
        viewModelScope.launch {
            doesImageExistsInLocalDB.value = localRoverImagesRepository.doesThisImageExists(imgUrl)
        }
    }

    fun addANewImageInLocalDB(roverImage: RoverImage) {
        viewModelScope.launch {
            localRoverImagesRepository.addANewImage(roverImage)
            doesThisImageExists(roverImage.imgUrl)
        }
    }

    fun deleteAnExistingImageFromLocalDB(imgUrl: String) {
        viewModelScope.launch {
            localRoverImagesRepository.deleteAnImage(imgUrl)
            doesThisImageExists(imgUrl)
        }
    }
}