package com.sakethh.jetspacer.screens.space.apod

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.space.apod.remote.data.APODPaginationFetching
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class APODScreenVM(private val apodPaginationFetching: APODPaginationFetching = APODPaginationFetching()) :
    ViewModel() {
    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    val _dataForAPODPagination = mutableStateOf<List<APOD_DTO>>(emptyList())
    val dataForPagination = mutableStateOf<List<APOD_DTO>>(emptyList())
    val isDataForAPODPaginationLoaded = mutableStateOf(false)


    suspend fun fetchAPODData() {
        _dataForAPODPagination.value = apodPaginationFetching.getPaginatedAPODATA().component1().reversed()
        dataForPagination.value += _dataForAPODPagination.value
        isDataForAPODPaginationLoaded.value = true
    }

    suspend fun refreshData() {
        _dataForAPODPagination.value = emptyList()
        dataForPagination.value = emptyList()
        APODPaginationFetching.APODPaginationUtils.currentAPODDate=""
        APODPaginationFetching.APODPaginationUtils.currentFetchedCount.value=0
        APODPaginationFetching.APODPaginationUtils.initialFetchingValue=0
        APODPaginationFetching.APODPaginationUtils.primaryInitForAPODEndDate=0
        fetchAPODData()
    }
}