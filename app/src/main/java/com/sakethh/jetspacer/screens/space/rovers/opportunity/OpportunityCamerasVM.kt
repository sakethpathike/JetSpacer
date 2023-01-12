package com.sakethh.jetspacer.screens.space.rovers.opportunity

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.screens.space.rovers.RoverCameras
import com.sakethh.jetspacer.screens.space.rovers.opportunity.cameras.random.RandomOpportunityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.screens.space.rovers.opportunity.cameras.minites.MinitesOpportunityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.opportunity.cameras.pancam.PANCAMOpportunityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.opportunity.cameras.fhaz.FHAZOpportunityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.opportunity.cameras.navcam.NAVCAMOpportunityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.opportunity.cameras.rhaz.RHAZOpportunityCameraScreen

class OpportunityCamerasVM(private val opportunityCamerasImplementation: OpportunityCamerasImplementation = OpportunityCamerasImplementation()) :
    ViewModel() {

    val opportunityRoverCameras = listOf(
        RoverCameras(name = "Random", composable = { RandomOpportunityCameraScreen() }),
        RoverCameras(name = "FHAZ", composable = { FHAZOpportunityCameraScreen() }),
        RoverCameras(name = "RHAZ", composable = { RHAZOpportunityCameraScreen() }),
        RoverCameras(name = "NAVCAM", composable = { NAVCAMOpportunityCameraScreen() }),
        RoverCameras(name = "PANCAM", composable = { PANCAMOpportunityCameraScreen() }),
        RoverCameras(name = "MINITES", composable = { MinitesOpportunityCameraScreen() }),
    )

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


    suspend fun retrieveOpportunityCameraData(cameraName: OpportunityCameras, sol: Int, page: Int) {
        when (cameraName) {
            OpportunityCameras.RHAZ -> {
                _rhazDataFromAPI.value = opportunityCamerasImplementation.getRHAZData(sol, page)
                rhazDataFromAPI.value += _rhazDataFromAPI.value
                isRHAZCamDataLoaded.value = true
            }
            OpportunityCameras.FHAZ -> {
                _fhazDataFromAPI.value = opportunityCamerasImplementation.getFHAZData(sol, page)
                fhazDataFromAPI.value += _fhazDataFromAPI.value
                isFHAZDataLoaded.value = true
            }
            OpportunityCameras.NAVCAM -> {
                _navcamDataFromAPI.value = opportunityCamerasImplementation.getNAVCAMData(sol, page)
                navcamDataFromAPI.value += _navcamDataFromAPI.value
                isNAVCAMDataLoaded.value = true
            }
            OpportunityCameras.MINITES -> {
                _minitesDataFromAPI.value =
                    opportunityCamerasImplementation.getMINITESData(sol, page)
                minitesDataFromAPI.value += _minitesDataFromAPI.value
                isMinitesDataLoaded.value = true
            }
            OpportunityCameras.PANCAM -> {
                _pancamDataFromAPI.value = opportunityCamerasImplementation.getPANCAMData(sol, page)
                pancamDataFromAPI.value += _pancamDataFromAPI.value
                isPancamDataLoaded.value = true
            }
            OpportunityCameras.RANDOM -> {
                _randomCameraData.value = opportunityCamerasImplementation.getRandomCamerasData(sol, page).photos
                randomCameraDataFromAPI.value += _randomCameraData.value
                isRandomCamerasDataLoaded.value = true
            }
        }
    }

    fun clearOpportunityCameraData(cameraName: OpportunityCameras) {
        when (cameraName) {
            OpportunityCameras.RHAZ -> {
                rhazDataFromAPI.value = emptyList()
            }
            OpportunityCameras.FHAZ -> {
                fhazDataFromAPI.value = emptyList()
            }
            OpportunityCameras.NAVCAM -> {
                navcamDataFromAPI.value = emptyList()
            }
            OpportunityCameras.MINITES -> {
                minitesDataFromAPI.value = emptyList()
            }
            OpportunityCameras.PANCAM -> {
                pancamDataFromAPI.value = emptyList()
            }
            OpportunityCameras.RANDOM -> {
                randomCameraDataFromAPI.value = emptyList()
            }
        }
    }

    enum class OpportunityCameras {
        FHAZ, RHAZ, NAVCAM, PANCAM, MINITES, RANDOM
    }
}