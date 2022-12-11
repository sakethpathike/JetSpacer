package com.sakethh.jetspacer.screens.space.rovers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.CuriosityRoverScreen
import com.sakethh.jetspacer.screens.space.rovers.opportunity.OpportunityRoverScreen
import com.sakethh.jetspacer.screens.space.rovers.spirit.SpiritRoverScreen

data class RoversScreen(val screenName: String, val composable: @Composable () -> Unit)
class RoversScreenVM : ViewModel() {
    val listForDrawerContent = listOf(
        RoversScreen(screenName = "Curiosity", composable = { CuriosityRoverScreen() }),
        RoversScreen(screenName = "Opportunity", composable = { OpportunityRoverScreen() }),
        RoversScreen(screenName = "Spirit", composable = { SpiritRoverScreen() }),
    )

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