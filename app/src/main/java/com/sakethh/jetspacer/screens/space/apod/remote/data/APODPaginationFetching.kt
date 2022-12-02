package com.sakethh.jetspacer.screens.space.apod.remote.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.sakethh.jetspacer.screens.home.data.remote.apod.APODImplementation
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.httpClient.HTTPClient
import com.sakethh.jetspacer.screens.space.apod.remote.data.APODPaginationFetching.APODPaginationUtils.calendar
import com.sakethh.jetspacer.screens.space.apod.remote.data.APODPaginationFetching.APODPaginationUtils.currentAPODDate
import com.sakethh.jetspacer.screens.space.apod.remote.data.APODPaginationFetching.APODPaginationUtils.initialFetchingValue
import com.sakethh.jetspacer.screens.space.apod.remote.data.APODPaginationFetching.APODPaginationUtils.primaryInitForAPODEndDate
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date

class APODPaginationFetching() {

    @SuppressLint("SimpleDateFormat")
    suspend fun getPaginatedAPODATA(): List<APOD_DTO> {
        val apodImplementation = APODImplementation(HTTPClient.ktorClient)
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
        currentAPODDate = apodImplementation.getAPOD().date.toString()
        val stringToDate = dateFormat.parse(currentAPODDate)
        val endDate = if (primaryInitForAPODEndDate != 0) {
            dateFormat.format(stringToDate?.time?.minus(15))
        } else {
            dateFormat.format(stringToDate?.time)
        }
        val apodURL =
            "https://api.nasa.gov/planetary/apod?api_key=Ffr9YBia9lLW9vWQgzNvzKtKfGlUNvynVvF0UOcf&start_date=$startDate&end_date=$endDate"
        primaryInitForAPODEndDate = 1
        val apodData = mutableListOf<APOD_DTO>()
        APODImplementation(
            HTTPClient.ktorClient,
            apodURL = apodURL
        ).getAPODForPaginatedList().reversed().forEach {
            if (it.media_type.toString() == "image") {
                apodData.add(it)
            }
        }
        return apodData

    }

    object APODPaginationUtils {
        val calendar: Calendar = Calendar.getInstance()
        val currentFetchedCount = mutableStateOf(0)
        var initialFetchingValue = 0
        var currentAPODDate = ""
        var primaryInitForAPODEndDate = 0
    }

    init {
        val apodImplementation = APODImplementation(HTTPClient.ktorClient)
        CoroutineScope(Dispatchers.Default).launch {
            currentAPODDate = apodImplementation.getAPOD().date.toString()
        }
    }
}