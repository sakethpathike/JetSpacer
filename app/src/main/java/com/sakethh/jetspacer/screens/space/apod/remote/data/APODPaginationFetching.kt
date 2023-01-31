package com.sakethh.jetspacer.screens.space.apod.remote.data

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.screens.home.data.remote.apod.APODImplementation
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.apod.remote.data.APODPaginationFetching.APODPaginationUtils.calendar
import com.sakethh.jetspacer.screens.space.apod.remote.data.APODPaginationFetching.APODPaginationUtils.currentAPODDate
import com.sakethh.jetspacer.screens.space.apod.remote.data.APODPaginationFetching.APODPaginationUtils.initialFetchingValue
import com.sakethh.jetspacer.screens.space.apod.remote.data.APODPaginationFetching.APODPaginationUtils.primaryInitForAPODEndDate
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class APODPaginationFetching(private val apodImplementation: APODImplementation = APODImplementation(HTTPClient.ktorClientWithCache)) {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    suspend fun getPaginatedAPODATA(): List<List<APOD_DTO>> {
        var exceptionWhilePickingAPODDate=false
        val fetchingLimit = 15
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        initialFetchingValue += fetchingLimit
        APODPaginationUtils.currentFetchedCount.value = initialFetchingValue
        calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE) - initialFetchingValue
        )
        calendar.add(Calendar.DATE, -initialFetchingValue)
        val startDate = dateFormat.format(calendar.time)
        currentAPODDate = apodImplementation.getAPOD().date ?: LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE).toString()
        val stringToDate = dateFormat.parse(currentAPODDate) ?: Date()
        val endDate = if (primaryInitForAPODEndDate != 0) {
            dateFormat.format(stringToDate.time.minus(15))
        } else {
            dateFormat.format(stringToDate.time.minus(2))
        }
        val apodURL = "https://api.nasa.gov/planetary/apod?api_key=Ffr9YBia9lLW9vWQgzNvzKtKfGlUNvynVvF0UOcf&start_date=$startDate&end_date=$endDate"
        primaryInitForAPODEndDate = 1
        val _apodData = mutableListOf<Deferred<List<APOD_DTO>>>()
        try {
            coroutineScope {
                val apodData = async {
                    APODImplementation(
                        HTTPClient.ktorClientWithCache,
                        apodURL = apodURL
                    ).getAPODForPaginatedList().component1()
                }
                _apodData.add(apodData)
            }
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
        } catch (_: Exception) {
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
        }
        return _apodData.awaitAll()
    }

    object APODPaginationUtils {
        val calendar: Calendar = Calendar.getInstance()
        val currentFetchedCount = mutableStateOf(0)
        var initialFetchingValue = 0
        var currentAPODDate = ""
        var primaryInitForAPODEndDate = 0
    }
}