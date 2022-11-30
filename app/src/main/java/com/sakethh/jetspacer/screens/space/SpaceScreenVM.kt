package com.sakethh.jetspacer.screens.space

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.data.remote.apod.APODFetching
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.space.remote.data.SpaceScreenImplementation
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpaceScreenVM(
    private val spaceScreenImplementation: SpaceScreenImplementation = SpaceScreenImplementation(),
    private val apodFetching: APODFetching = APODFetching(),
) :
    ViewModel() {
    val apodDateData = mutableStateOf(APOD_DTO())
    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }

    init {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            apodDateData.value = apodFetching.getAPOD()
        }
    }

    suspend fun getAPODDateData(apodDateForURL: String) {
        apodDateData.value =
            spaceScreenImplementation.getAPODSpecificDateData(apodDateForURL = apodDateForURL)
    }
}