package com.sakethh.jetspacer.screens.space.rovers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.CuriosityRoverScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.CuriosityCamerasScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.CuriosityCamerasVM
import com.sakethh.jetspacer.screens.space.rovers.opportunity.OpportunityMainRoverScreen
import com.sakethh.jetspacer.screens.space.rovers.spirit.SpiritRoverMainScreen

data class RoversScreen(val screenName: String, val composable: @Composable () -> Unit)
data class RoverCameras(val name: String, val composable: @Composable () -> Unit)
class RoversScreenVM : ViewModel() {
    val listForDrawerContent = listOf(
        RoversScreen(screenName = "Curiosity", composable = { CuriosityRoverScreen() }),
        RoversScreen(screenName = "Opportunity", composable = { OpportunityMainRoverScreen() }),
        RoversScreen(screenName = "Spirit", composable = { SpiritRoverMainScreen() }),
    )
    val curiosityRoverCameras = listOf(
        RoverCameras(
            name = "Random",
            composable = { CuriosityCamerasScreen(cameraName = CuriosityCamerasVM.CuriosityCameras.RANDOM) }),
        RoverCameras(
            name = "FHAZ",
            composable = { CuriosityCamerasScreen(cameraName = CuriosityCamerasVM.CuriosityCameras.FHAZ) }),
        RoverCameras(
            name = "RHAZ",
            composable = { CuriosityCamerasScreen(cameraName = CuriosityCamerasVM.CuriosityCameras.RHAZ) }),
        RoverCameras(
            name = "MAST",
            composable = { CuriosityCamerasScreen(cameraName = CuriosityCamerasVM.CuriosityCameras.MAST) }),
        RoverCameras(
            name = "CHEMCAM",
            composable = { CuriosityCamerasScreen(cameraName = CuriosityCamerasVM.CuriosityCameras.CHEMCAM) }),
        RoverCameras(
            name = "MAHLI",
            composable = { CuriosityCamerasScreen(cameraName = CuriosityCamerasVM.CuriosityCameras.MAHLI) }),
        RoverCameras(
            name = "MARDI",
            composable = { CuriosityCamerasScreen(cameraName = CuriosityCamerasVM.CuriosityCameras.MARDI) }),
        RoverCameras(
            name = "NAVCAM",
            composable = { CuriosityCamerasScreen(cameraName = CuriosityCamerasVM.CuriosityCameras.NAVCAM) }),
    )
    val atLastIndexInLazyVerticalGrid = mutableStateOf(false)

    val imgURL = mutableStateOf("")
    val capturedOn = mutableStateOf("")
    val cameraName = mutableStateOf("")
    val sol = mutableStateOf("")
    val earthDate = mutableStateOf("")
    val roverName = mutableStateOf("")
    val roverStatus = mutableStateOf("")
    val launchingDate = mutableStateOf("")
    val landingDate = mutableStateOf("")
    val shouldBtmSheetVisible = mutableStateOf(false)

    object RoverScreenUtils {
        val paddingValues = mutableStateOf(PaddingValues(0.dp))
    }

    fun openRoverBtmSheet(
        imgURL: String,
        capturedOn: String,
        cameraName: String,
        sol: String,
        earthDate: String,
        roverName: String,
        roverStatus: String,
        launchingDate: String,
        landingDate: String
    ) {
        this.imgURL.value = imgURL
        this.capturedOn.value = capturedOn
        this.cameraName.value = cameraName
        this.sol.value = sol
        this.earthDate.value = earthDate
        this.roverName.value = roverName
        this.roverStatus.value = roverStatus
        this.launchingDate.value = launchingDate
        this.landingDate.value = landingDate
        this.shouldBtmSheetVisible.value = true
    }


}