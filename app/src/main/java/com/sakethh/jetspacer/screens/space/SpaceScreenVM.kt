package com.sakethh.jetspacer.screens.space

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.data.remote.apod.APODFetching
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.space.remote.data.SpaceScreenImplementation
import com.sakethh.jetspacer.screens.space.remote.data.marsWeather.dto.MarsWeatherDTO
import kotlinx.coroutines.*

class SpaceScreenVM(
    private val spaceScreenImplementation: SpaceScreenImplementation = SpaceScreenImplementation(),
    private val apodFetching: APODFetching = APODFetching(),
) :
    ViewModel() {
    val apodDateData = mutableStateOf(APOD_DTO())
    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    val marsWeatherDTO = mutableStateOf(MarsWeatherDTO())

    val btmSheetType= mutableStateOf(HomeScreenViewModel.BtmSheetType.Details)

    init {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            loadData()
        }
    }

    suspend fun loadData() {
        apodDateData.value = apodFetching.getAPOD()
        marsWeatherDTO.value = spaceScreenImplementation.getMarsWeatherData()
    }

    suspend fun getAPODDateData(apodDateForURL: String) {
        apodDateData.value =
            spaceScreenImplementation.getAPODSpecificDateData(apodDateForURL = apodDateForURL)
    }
}