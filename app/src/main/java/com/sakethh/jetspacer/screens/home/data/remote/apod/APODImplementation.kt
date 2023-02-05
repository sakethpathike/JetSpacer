package com.sakethh.jetspacer.screens.home.data.remote.apod

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class APODImplementation(
    private val ktorClient: HttpClient,
    private val apodURL: String,
) : APODService{
    override suspend fun getAPOD(): APOD_DTO {
        return try {
            HomeScreenViewModel.Network.isConnectionSucceed.value = true
            ktorClient.get(apodURL).body()
        } catch (_: Exception) {
            HomeScreenViewModel.Network.isConnectionSucceed.value = false
            APOD_DTO()
        }
    }

    override suspend fun getAPODForPaginatedList(): List<List<APOD_DTO>> {
        val data = mutableStateListOf<Deferred<List<APOD_DTO>>>()
        coroutineScope {
            val _data = async {
                try {
                    HomeScreenViewModel.Network.isConnectionSucceed.value = true
                    ktorClient.get(apodURL).body<List<APOD_DTO>>()
                } catch (_: Exception) {
                    HomeScreenViewModel.Network.isConnectionSucceed.value = false
                    emptyList()
                }
            }
            data.add(_data)
        }
        return data.awaitAll()
    }
}