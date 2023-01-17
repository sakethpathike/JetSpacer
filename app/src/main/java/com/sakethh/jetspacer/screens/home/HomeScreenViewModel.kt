@file:Suppress("LocalVariableName")

package com.sakethh.jetspacer.screens.home

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.localDB.APOD_DB_DTO
import com.sakethh.jetspacer.localDB.DBImplementation
import com.sakethh.jetspacer.localDB.MarsRoversDBDTO
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
    private val apodFetching: APODFetching = APODFetching(),
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
    val bookMarkIcons = mutableStateOf<ImageVector>(Icons.Outlined.BookmarkAdd)
    val bookMarkText = mutableStateOf("")
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
            awaitAll(
                async { ipGeolocationFetching.getGeoLocationData() },
                async { issLocationFetching.getISSLatitudeAndLongitude() },
                async { apodFetching.getAPOD() }
            )
            val geoLocationData = async { ipGeolocationFetching.getGeoLocationData() }
            val issLocationData = async { issLocationFetching.getISSLatitudeAndLongitude() }
            val apodData = async { apodFetching.getAPOD() }
            geolocationDTODataFromAPI.value = geoLocationData.await()
            this@HomeScreenViewModel.apodDataFromAPI.value = apodData.await()
            issLocationData.await().collect { _issLocationData ->
                this@HomeScreenViewModel._issLocationFromAPIFlow.emit(_issLocationData)
            }
        }
        this.viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            ipGeolocationFetching.getAstronomicalData().collect { ipGeolocationData ->
                this@HomeScreenViewModel._astronomicalDataFromAPIFlow.emit(ipGeolocationData)
            }
        }

        if (apodDataFromAPI.value.url.toString().contains(regex = Regex("/apod.nasa.gov/"))) {
            doesThisExistsInAPODIconTxt(apodDataFromAPI.value.url.toString())
        }
    }

    fun doesThisExistsInAPODIconTxt(imageURL: String) {
        if (true) {
            bookMarkText.value = "Add to bookmarks"
            bookMarkIcons.value = Icons.Outlined.BookmarkAdd
        } else {
            bookMarkText.value = "Remove from bookmarks"
            bookMarkIcons.value = Icons.Outlined.BookmarkRemove
        }
    }
    fun doesThisExistsInRoverDBIconTxt(imageURL: String) {
        if (true) {
            bookMarkText.value = "Add to bookmarks"
            bookMarkIcons.value = Icons.Outlined.BookmarkAdd
        } else {
            bookMarkText.value = "Remove from bookmarks"
            bookMarkIcons.value = Icons.Outlined.BookmarkRemove
        }
    }




    object BookMarkUtils {
        val isAlertDialogEnabledForAPODDB = mutableStateOf(false)
        val isAlertDialogEnabledForRoversDB = mutableStateOf(false)
    }
}
