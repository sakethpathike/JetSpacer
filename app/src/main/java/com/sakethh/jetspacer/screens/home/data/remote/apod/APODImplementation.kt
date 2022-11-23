package com.sakethh.jetspacer.screens.home.data.remote.apod

import androidx.compose.runtime.mutableStateOf
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class APODImplementation(
    private val ktorClient: HttpClient,
    private val apodURL: String = Constants.APOD_URL
) : APODService {
    override suspend fun getAPOD(): APOD_DTO {
        return ktorClient.get(apodURL).body()
    }

    override suspend fun getAPODForPaginatedList(): List<APOD_DTO> {
        val apodList = mutableStateOf<List<APOD_DTO>>(emptyList())
        coroutineScope {
            val _data = async { ktorClient.get(apodURL).body<List<APOD_DTO>>() }
            apodList.value = _data.await()
        }
        return apodList.value
    }
}