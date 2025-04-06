package com.sakethh.jetspacer.ui.explore.apodArchive.apodBtmSheet

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.data.repository.LocalAPODImplementation
import com.sakethh.jetspacer.domain.model.APOD
import com.sakethh.jetspacer.domain.repository.LocalAPODRepository
import kotlinx.coroutines.launch

class APODBtmSheetViewModel(
    private val localAPODRepository: LocalAPODRepository = LocalAPODImplementation()
) : ViewModel() {
    val doesAPODExistsInLocalDB = mutableStateOf(false)

    fun doesAPODExistsInLocalDB(date: String) {
        viewModelScope.launch {
            doesAPODExistsInLocalDB.value =
                localAPODRepository.doesThisDateExistsInTheSavedAPODs(date)
        }
    }

    fun addANewAPODInLocalDB(apod: APOD) {
        viewModelScope.launch {
            localAPODRepository.addANewAPOD(apod)
            doesAPODExistsInLocalDB(apod.date)
        }
    }

    fun deleteAnAPOD(date: String) {
        viewModelScope.launch {
            localAPODRepository.deleteAnAPOD(date)
            doesAPODExistsInLocalDB(date)
        }
    }
}