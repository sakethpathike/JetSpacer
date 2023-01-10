package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.chemcam

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.CuriosityCamerasVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.ModifiedLazyVerticalGrid
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.SolTextField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ChemCamCuriosityCameraScreen() {
    val curiosityCameraVM: CuriosityCamerasVM = viewModel()
    val roversScreenVM: RoversScreenVM = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val solImagesData = curiosityCameraVM.chemcamDataFromAPI.value
    LaunchedEffect(key1 = true) {
        curiosityCameraVM.getCHEMCAMData(
            sol = CHEMCAMCuriosityCameraScreen.solValue.value.toInt(),
            page = 0
        )
    }
    Scaffold(floatingActionButtonPosition = FabPosition.Center, floatingActionButton = {
        if (solImagesData.isNotEmpty() && curiosityCameraVM._chemcamDataFromAPI.value.isEmpty() && curiosityCameraVM.isChemCamDataLoaded.value && roversScreenVM.atLastIndexInLazyVerticalGrid.value) {
            Snackbar(
                containerColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 50.dp)
                    .wrapContentHeight()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = "You've reached the end, change the sol value to explore more!",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    softWrap = true,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    lineHeight = 18.sp
                )
            }
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            SolTextField(solValue = CHEMCAMCuriosityCameraScreen.solValue, onContinueClick = {
                CHEMCAMCuriosityCameraScreen.currentPage = 0
                curiosityCameraVM.isChemCamDataLoaded.value = false
                curiosityCameraVM.clearCuriosityCameraData(cameraName = CuriosityCamerasVM.CuriosityCameras.CHEMCAM)
                coroutineScope.launch {
                    curiosityCameraVM.getCHEMCAMData(CHEMCAMCuriosityCameraScreen.solValue.value.toInt(), 0)
                }
            })
            if (!curiosityCameraVM.isChemCamDataLoaded.value) {
                StatusScreen(
                    title = "Wait a moment!",
                    description = "fetching the images from this camera that were captured on sol ${CHEMCAMCuriosityCameraScreen.solValue.value}",
                    status = Status.LOADING
                )

            } else if (solImagesData.isEmpty()) {
                StatusScreen(
                    title = "4ooooFour",
                    description = "No images were captured by this camera on sol ${CHEMCAMCuriosityCameraScreen.solValue.value}. Change the sol value; it may give results.",
                    status = Status.FOURO4InMarsScreen
                )
            } else {
                ModifiedLazyVerticalGrid(listData = solImagesData, loadMoreButtonBooleanExpression = curiosityCameraVM._chemcamDataFromAPI.value.isNotEmpty() && curiosityCameraVM.isChemCamDataLoaded.value) {
                    coroutineScope.launch {
                        curiosityCameraVM.getCHEMCAMData(
                            sol = CHEMCAMCuriosityCameraScreen.solValue.value.toInt(),
                            page = CHEMCAMCuriosityCameraScreen.currentPage++
                        )
                    }
                }
            }

        }
    }
}
object CHEMCAMCuriosityCameraScreen {
    var solValue = mutableStateOf("0")
    var currentPage = 0
}