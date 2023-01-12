package com.sakethh.jetspacer.screens.space.rovers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.CuriosityRoverScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.chemcam.ChemCamCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.fhaz.FHAZCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.mahli.MAHLICuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.mardi.MARDICuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.mast.MASTCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.navcam.NAVCAMCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RandomCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.rhaz.RHAZCuriosityCameraScreen
import com.sakethh.jetspacer.screens.space.rovers.opportunity.OpportunityRoverScreen
import com.sakethh.jetspacer.screens.space.rovers.spirit.SpiritRoverScreen

data class RoversScreen(val screenName: String, val composable: @Composable () -> Unit)
data class RoverCameras(val name: String, val composable: @Composable () -> Unit)
class RoversScreenVM : ViewModel() {
    val listForDrawerContent = listOf(
        RoversScreen(screenName = "Curiosity", composable = { CuriosityRoverScreen() }),
        RoversScreen(screenName = "Opportunity", composable = { OpportunityRoverScreen() }),
        RoversScreen(screenName = "Spirit", composable = { SpiritRoverScreen() }),
    )
    val curiosityRoverCameras = listOf(
        RoverCameras(name = "Random", composable = { RandomCuriosityCameraScreen() }),
        RoverCameras(name = "FHAZ", composable = { FHAZCuriosityCameraScreen() }),
        RoverCameras(name = "RHAZ", composable = { RHAZCuriosityCameraScreen() }),
        RoverCameras(name = "MAST", composable = { MASTCuriosityCameraScreen() }),
        RoverCameras(name = "CHEMCAM", composable = { ChemCamCuriosityCameraScreen() }),
        RoverCameras(name = "MAHLI", composable = { MAHLICuriosityCameraScreen() }),
        RoverCameras(name = "MARDI", composable = { MARDICuriosityCameraScreen() }),
        RoverCameras(name = "NAVCAM", composable = { NAVCAMCuriosityCameraScreen() }),
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