package com.sakethh.jetspacer.screens.space.rovers.spirit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.CurrentHTTPCodes
import com.sakethh.jetspacer.screens.space.rovers.RoverCameras
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.screens.space.rovers.opportunity.OpportunityCamerasVM
import com.sakethh.jetspacer.screens.space.rovers.spirit.cameras.SpiritRoverSubScreen

class SpiritCamerasVM(private val spiritCamerasImplementation: SpiritCamerasImplementation = SpiritCamerasImplementation()) :
    ViewModel() {

    val spiritRoverCameras = listOf(
        RoverCameras(
            name = "Random",
            composable = { SpiritRoverSubScreen(cameraName = SpiritCameras.RANDOM) }),
        RoverCameras(
            name = "FHAZ",
            composable = { SpiritRoverSubScreen(cameraName = SpiritCameras.FHAZ) }),
        RoverCameras(
            name = "RHAZ",
            composable = { SpiritRoverSubScreen(cameraName = SpiritCameras.RHAZ) }),
        RoverCameras(
            name = "NAVCAM",
            composable = { SpiritRoverSubScreen(cameraName = SpiritCameras.NAVCAM) }),
        RoverCameras(
            name = "PANCAM",
            composable = { SpiritRoverSubScreen(cameraName = SpiritCameras.PANCAM) }),
        RoverCameras(
            name = "MINITES",
            composable = { SpiritRoverSubScreen(cameraName = SpiritCameras.MINITES) }))

    val _randomCameraData = mutableStateOf<List<Photo>>(emptyList())
    val randomCameraDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isRandomCamerasDataLoaded = mutableStateOf(false)

    val _fhazDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val fhazDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isFHAZDataLoaded = mutableStateOf(false)

    val _pancamDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val pancamDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isPancamDataLoaded = mutableStateOf(false)

    val _minitesDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val minitesDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isMinitesDataLoaded = mutableStateOf(false)

    val _rhazDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val rhazDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isRHAZCamDataLoaded = mutableStateOf(false)


    val _navcamDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val navcamDataFromAPI = mutableStateOf<List<Photo>>(emptyList())
    val isNAVCAMDataLoaded = mutableStateOf(false)


    init {
        CurrentHTTPCodes.marsRoversDataHTTPCode.value=200
    }

    suspend fun retrieveSpiritCameraData(cameraName: SpiritCameras, sol: Int, page: Int) {
        when (cameraName) {
            SpiritCameras.RHAZ -> {
                _rhazDataFromAPI.value = spiritCamerasImplementation.getRHAZData(sol, page)
                rhazDataFromAPI.value += _rhazDataFromAPI.value
                isRHAZCamDataLoaded.value = true
            }
            SpiritCameras.FHAZ -> {
                _fhazDataFromAPI.value = spiritCamerasImplementation.getFHAZData(sol, page)
                fhazDataFromAPI.value += _fhazDataFromAPI.value
                isFHAZDataLoaded.value = true
            }
            SpiritCameras.NAVCAM -> {
                _navcamDataFromAPI.value = spiritCamerasImplementation.getNAVCAMData(sol, page)
                navcamDataFromAPI.value += _navcamDataFromAPI.value
                isNAVCAMDataLoaded.value = true
            }
            SpiritCameras.MINITES -> {
                _minitesDataFromAPI.value =
                    spiritCamerasImplementation.getMINITESData(sol, page)
                minitesDataFromAPI.value += _minitesDataFromAPI.value
                isMinitesDataLoaded.value = true
            }
            SpiritCameras.PANCAM -> {
                _pancamDataFromAPI.value = spiritCamerasImplementation.getPANCAMData(sol, page)
                pancamDataFromAPI.value += _pancamDataFromAPI.value
                isPancamDataLoaded.value = true
            }
            SpiritCameras.RANDOM -> {
                _randomCameraData.value =
                    spiritCamerasImplementation.getRandomCamerasData(sol, page).photos
                randomCameraDataFromAPI.value += _randomCameraData.value
                isRandomCamerasDataLoaded.value = true
            }
        }
    }

    fun clearSpiritCameraData(cameraName: SpiritCameras) {
        when (cameraName) {
            SpiritCameras.RHAZ -> {
                rhazDataFromAPI.value = emptyList()
            }
            SpiritCameras.FHAZ -> {
                fhazDataFromAPI.value = emptyList()
            }
            SpiritCameras.NAVCAM -> {
                navcamDataFromAPI.value = emptyList()
            }
            SpiritCameras.MINITES -> {
                minitesDataFromAPI.value = emptyList()
            }
            SpiritCameras.PANCAM -> {
                pancamDataFromAPI.value = emptyList()
            }
            SpiritCameras.RANDOM -> {
                randomCameraDataFromAPI.value = emptyList()
            }
        }
    }

    enum class SpiritCameras {
        FHAZ, RHAZ, NAVCAM, PANCAM, MINITES, RANDOM
    }
}