@file:Suppress("LocalVariableName")

package com.sakethh.jetspacer.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.home.data.remote.apod.APODFetching
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.IPGeolocationFetching
import com.sakethh.jetspacer.screens.home.data.remote.ipGeoLocation.dto.IPGeoLocationDTO
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.ISSLocationFetching
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.ISSLocationDTO
import com.sakethh.jetspacer.screens.home.data.remote.issLocation.dto.IssPosition
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.Calendar

@Suppress("ObjectPropertyName")
@SuppressLint("LongLogTag")
open class HomeScreenViewModel(
    private val ipGeolocationFetching: IPGeolocationFetching = IPGeolocationFetching(),
    private val issLocationFetching: ISSLocationFetching = ISSLocationFetching(),
    private val apodFetching: APODFetching = APODFetching()
) : ViewModel() {
    var currentPhaseOfDay: String = ""
    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    val geolocationDTODataFromAPI = mutableStateOf(IPGeoLocationDTO())
    private val _astronomicalDataFromAPIFlow = MutableStateFlow(IPGeoLocationDTO())
    val astronomicalDataFromAPIFlow = _astronomicalDataFromAPIFlow.asStateFlow()
    private val _issLocationFromAPIFlow =
        MutableStateFlow(ISSLocationDTO(IssPosition("", ""), "", 0))
    val issLocationFromAPIFlow = _issLocationFromAPIFlow.asStateFlow()
    val apodDataFromAPI = mutableStateOf(APOD_DTO("", "", "", "", "", "", ""))
    val doesAPODExistsInDB = mutableStateOf(false)


    init {

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

        this.viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            awaitAll(
                async {
                    geolocationDTODataFromAPI.value = ipGeolocationFetching.getGeoLocationData()
                },
                async {
                    issLocationFetching.getISSLatitudeAndLongitude()
                        .collect { _issLocationData ->
                            this@HomeScreenViewModel._issLocationFromAPIFlow.emit(
                                _issLocationData
                            )
                        }
                },
                async {
                    this@HomeScreenViewModel.apodDataFromAPI.value = apodFetching.getAPOD()
                },
                async {
                    ipGeolocationFetching.getAstronomicalData().collect { ipGeolocationData ->
                        this@HomeScreenViewModel._astronomicalDataFromAPIFlow.emit(
                            ipGeolocationData
                        )
                    }
                }
            )
        }
    }

    object Network {
        private val _connectedToInternet = MutableStateFlow(true)
        val connectedToInternet = _connectedToInternet.asStateFlow()
        val isConnectionSucceed = mutableStateOf(true)
        suspend fun isConnectedToInternet() {

            while (true) {
                try {
                    val isHTTP200 =
                        HTTPClient.ktorClientWithoutCache.get("https://google.com").status.value == 200
                    isConnectionSucceed.value = true
                    _connectedToInternet.emit(isHTTP200)
                    Log.d("HTTP CODE", isHTTP200.toString())
                } catch (_: Exception) {
                    isConnectionSucceed.value = false
                }
                delay(2000L)
            }

        }
    }


    object BookMarkUtils {
        val isAlertDialogEnabledForAPODDB = mutableStateOf(false)
        val isAlertDialogEnabledForRoversDB = mutableStateOf(false)
    }
}
