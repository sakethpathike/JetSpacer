package com.sakethh.jetspacer.screens.space

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.CurrentHTTPCodes
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

    val isCustomAPODLoaded = mutableStateOf(false)
    init {
        CurrentHTTPCodes.apodCurrentHTTPCode.value=200
        CurrentHTTPCodes.apodParticularDateDataHTTPCode.value=200
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionalHandler) {
            loadData()
        }
    }

    suspend fun loadData() {
        isCustomAPODLoaded.value=false
        apodDateData.value = apodFetching.getAPOD()
        isCustomAPODLoaded.value=true
        // marsWeatherDTO.value = spaceScreenImplementation.getMarsWeatherData()
    }

    suspend fun getAPODDateData(apodDateForURL: String) {
        isCustomAPODLoaded.value=false
        CurrentHTTPCodes.apodParticularDateDataHTTPCode.value=200
        apodDateData.value =
            spaceScreenImplementation.getAPODSpecificDateData(apodDateForURL = apodDateForURL)
        isCustomAPODLoaded.value=true
    }
}