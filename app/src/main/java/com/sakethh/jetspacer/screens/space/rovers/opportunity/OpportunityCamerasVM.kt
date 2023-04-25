package com.sakethh.jetspacer.screens.space.rovers.opportunity

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.CurrentHTTPCodes
import com.sakethh.jetspacer.screens.space.rovers.RoverCameras
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.screens.space.rovers.opportunity.cameras.OpportunityRoverSubScreen

class OpportunityCamerasVM(private val opportunityCamerasImplementation: OpportunityCamerasImplementation = OpportunityCamerasImplementation()) :
    ViewModel() {

    val opportunityRoverCameras = listOf(
        RoverCameras(
            name = "Random",
            composable = { OpportunityRoverSubScreen(cameraName = OpportunityCameras.RANDOM) }),
        RoverCameras(
            name = "FHAZ",
            composable = { OpportunityRoverSubScreen(cameraName = OpportunityCameras.FHAZ) }),
        RoverCameras(
            name = "RHAZ",
            composable = { OpportunityRoverSubScreen(cameraName = OpportunityCameras.RHAZ) }),
        RoverCameras(
            name = "NAVCAM",
            composable = { OpportunityRoverSubScreen(cameraName = OpportunityCameras.NAVCAM) }),
        RoverCameras(
            name = "PANCAM",
            composable = { OpportunityRoverSubScreen(cameraName = OpportunityCameras.PANCAM) }),
        RoverCameras(
            name = "MINITES",
            composable = { OpportunityRoverSubScreen(cameraName = OpportunityCameras.MINITES) }),
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

    init {
        CurrentHTTPCodes.marsRoversDataHTTPCode.value=200
    }

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
                _randomCameraData.value =
                    opportunityCamerasImplementation.getRandomCamerasData(sol, page).photos
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