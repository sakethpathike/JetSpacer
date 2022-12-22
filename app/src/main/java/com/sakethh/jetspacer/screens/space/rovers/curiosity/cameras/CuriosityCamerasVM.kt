package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo

class CuriosityCamerasVM(private val curiosityCamerasImplementation: CuriosityCamerasImplementation = CuriosityCamerasImplementation()) :
    ViewModel() {
    val fhazDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val rhazDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val mastDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val chemcamDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val mahliDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val mardiDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val navcamDataFromAPI = mutableStateOf<List<Photo>>(emptyList())

    suspend fun getFHAZData(sol: Int, page: Int) {
        fhazDataFromAPI.value= curiosityCamerasImplementation.getFHAZData(sol, page)
    }

    suspend fun getRHAZData(sol: Int, page: Int) {
        rhazDataFromAPI.value= curiosityCamerasImplementation.getRHAZData(sol, page)
    }

    suspend fun getMASTData(sol: Int, page: Int) {
        mastDataFromAPI.value= curiosityCamerasImplementation.getMASTData(sol, page)
    }

    suspend fun getCHEMCAMData(sol: Int, page: Int) {
        chemcamDataFromAPI.value= curiosityCamerasImplementation.getCHEMCAMData(sol, page)

    }

    suspend fun getMAHLIData(sol: Int, page: Int) {
        mahliDataFromAPI.value= curiosityCamerasImplementation.getMAHLIData(sol, page)
    }

    suspend fun getMARDIData(sol: Int, page: Int) {
        mardiDataFromAPI.value= curiosityCamerasImplementation.getMARDIData(sol, page)
    }

    suspend fun getNAVCAMData(sol: Int, page: Int) {
        navcamDataFromAPI.value= curiosityCamerasImplementation.getNAVCAMData(sol, page)
    }
}