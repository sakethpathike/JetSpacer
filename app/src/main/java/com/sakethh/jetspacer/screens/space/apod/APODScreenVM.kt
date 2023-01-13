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

    init {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            fetchAPODData()
        }
    }

    suspend fun fetchAPODData() {
        _dataForAPODPagination.value = apodPaginationFetching.getPaginatedAPODATA()
        dataForPagination.value += _dataForAPODPagination.value
        isDataForAPODPaginationLoaded.value = true
    }
}