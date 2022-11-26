package com.sakethh.jetspacer.screens.space.apod

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.space.apod.remote.data.APODPaginationFetching
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class APODScreenVM(private val apodPaginationFetching: APODPaginationFetching = APODPaginationFetching()) :
    ViewModel() {
    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    private val _dataForPagination = MutableStateFlow<List<APOD_DTO>>(emptyList())
    val dataForPagination = _dataForPagination.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
           fetchAPODData()
        }
    }

    suspend fun fetchAPODData() {
        _dataForPagination.emit(apodPaginationFetching.getPaginatedAPODATA())
    }
}