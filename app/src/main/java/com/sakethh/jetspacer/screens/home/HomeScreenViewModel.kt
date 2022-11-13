@file:Suppress("LocalVariableName")

package com.sakethh.jetspacer.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.screens.home.data.remote.apod.APODFetching
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.IPGeolocationFetching
import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto.IPGeoLocationDTO
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.ISSLocationFetching
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.ISSLocationDTO
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.IssPosition
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.Calendar

@SuppressLint("LongLogTag")
class HomeScreenViewModel(
    private val ipGeolocationFetching: IPGeolocationFetching = IPGeolocationFetching(),
    private val issLocationFetching: ISSLocationFetching = ISSLocationFetching(),
    private val apodFetching: APODFetching = APODFetching()
) : ViewModel() {
    var currentPhaseOfDay: String = ""
    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    val geolocationDTODataFromAPI = mutableStateOf(IPGeoLocationDTO())
    val astronomicalDataFromAPIFlow = mutableStateOf<Flow<IPGeoLocationDTO>>(emptyFlow())
    val issLocationFromAPIFlow = mutableStateOf<Flow<ISSLocationDTO>>(emptyFlow())
    val apodDataFromAPI = mutableStateOf(APOD_DTO("", "", "", "", "", "", ""))
    private val _moonAltitude = MutableStateFlow(IPGeoLocationDTO())
    val moonAltitude = _moonAltitude.asStateFlow()

    init {
        this.viewModelScope.launch(Dispatchers.Default + coroutineExceptionalHandler) {
            when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                in 0..3 -> {
                    currentPhaseOfDay = "Didn't slept?"
                }
                in 4..11 -> {
                    currentPhaseOfDay = "Good Morning"
                }
                in 12..15 -> {
                    currentPhaseOfDay = "Good Afternoon"
                }
                in 16..22 -> {
                    currentPhaseOfDay = "Good Evening"
                }
                in 23 downTo 0 -> {
                    currentPhaseOfDay = "Good Night?"
                }
                else -> {
                    currentPhaseOfDay = "Everything fine?"
                }
            }
        }
        this.viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            val geoLocationData = async { ipGeolocationFetching.getGeoLocationData() }
            val issLocationData = async { issLocationFetching.getISSLatitudeAndLongitude() }
            val apodData = async { apodFetching.getAPOD() }
            val astronomicalData = async { ipGeolocationFetching.getAstronomicalData() }
            geolocationDTODataFromAPI.value = geoLocationData.await()
            issLocationFromAPIFlow.value = issLocationData.await()
            this@HomeScreenViewModel.apodDataFromAPI.value = apodData.await()
            this@HomeScreenViewModel.astronomicalDataFromAPIFlow.value = astronomicalData.await()
            ipGeolocationFetching.getAstronomicalData().collectLatest {
                _moonAltitude.emit(it)
            }
        }
    }
}
