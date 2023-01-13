package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RandomCuriosityCameraVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CuriosityCamerasVM(
    private val randomCuriosityCameraVM: RandomCuriosityCameraVM = RandomCuriosityCameraVM(),
    private val curiosityCamerasImplementation: CuriosityCamerasImplementation = CuriosityCamerasImplementation()
) :
    ViewModel() {

    val atNearlyLastImageAtLastSolPage = mutableStateOf(false)

    val _fhazDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val fhazDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isFHAZDataLoaded = mutableStateOf(false)

    val _rhazDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val rhazDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isRHAZCamDataLoaded = mutableStateOf(false)

    val _mastDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val mastDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isMASTCamDataLoaded = mutableStateOf(false)

    val _chemcamDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val chemcamDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isChemCamDataLoaded = mutableStateOf(false)

    val _mahliDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val mahliDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isMAHLIDataLoaded = mutableStateOf(false)

    val _mardiDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val mardiDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isMARDIDataLoaded = mutableStateOf(false)

    val _navcamDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val navcamDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isNAVCAMDataLoaded = mutableStateOf(false)

    suspend fun getFHAZData(sol: Int, page: Int) {
        _fhazDataFromAPI.value = curiosityCamerasImplementation.getFHAZData(sol, page)
        fhazDataFromAPI.value += _fhazDataFromAPI.value
        isFHAZDataLoaded.value = true
    }


    suspend fun getRHAZData(sol: Int, page: Int) {
        _rhazDataFromAPI.value = curiosityCamerasImplementation.getRHAZData(sol, page)
        rhazDataFromAPI.value += _rhazDataFromAPI.value
        isRHAZCamDataLoaded.value = true
    }


    suspend fun getMASTData(sol: Int, page: Int) {
        _mastDataFromAPI.value = curiosityCamerasImplementation.getMASTData(sol, page)
        mastDataFromAPI.value += _mastDataFromAPI.value
        isMASTCamDataLoaded.value = true
    }


    suspend fun getCHEMCAMData(sol: Int, page: Int) {
        _chemcamDataFromAPI.value = curiosityCamerasImplementation.getCHEMCAMData(sol, page)
        chemcamDataFromAPI.value += _chemcamDataFromAPI.value
        isChemCamDataLoaded.value = true
    }


    suspend fun getMAHLIData(sol: Int, page: Int) {
        _mahliDataFromAPI.value = curiosityCamerasImplementation.getMAHLIData(sol, page)
        mahliDataFromAPI.value += _mahliDataFromAPI.value
        isMAHLIDataLoaded.value = true
    }


    suspend fun getMARDIData(sol: Int, page: Int) {
        _mardiDataFromAPI.value = curiosityCamerasImplementation.getMARDIData(sol, page)
        mardiDataFromAPI.value += _mardiDataFromAPI.value
        isMARDIDataLoaded.value = true
    }


    suspend fun getNAVCAMData(sol: Int, page: Int) {
        _navcamDataFromAPI.value = curiosityCamerasImplementation.getNAVCAMData(sol, page)
        navcamDataFromAPI.value += _navcamDataFromAPI.value
        isNAVCAMDataLoaded.value = true
    }

    fun clearCuriosityCameraData(cameraName: CuriosityCameras) {
        when (cameraName) {
            CuriosityCameras.FHAZ -> {
                fhazDataFromAPI.value = emptyList()
            }
            CuriosityCameras.RHAZ -> {
                rhazDataFromAPI.value = emptyList()
            }
            CuriosityCameras.MAST -> {
                mastDataFromAPI.value = emptyList()
            }
            CuriosityCameras.CHEMCAM -> {
                chemcamDataFromAPI.value = emptyList()
            }
            CuriosityCameras.MAHLI -> {
                mahliDataFromAPI.value = emptyList()
            }
            CuriosityCameras.MARDI -> {
                mardiDataFromAPI.value = emptyList()
            }
            CuriosityCameras.NAVCAM -> {
                navcamDataFromAPI.value = emptyList()
            }
        }
    }

    enum class CuriosityCameras {
        FHAZ, RHAZ, MAST, CHEMCAM, MAHLI, MARDI, NAVCAM, RANDOM
    }
}