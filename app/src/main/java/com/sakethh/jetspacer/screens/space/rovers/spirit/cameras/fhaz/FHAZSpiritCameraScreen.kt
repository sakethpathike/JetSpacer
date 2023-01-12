package com.sakethh.jetspacer.screens.space.rovers.spirit.cameras.fhaz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.ModifiedLazyVerticalGrid
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.SolTextField
import com.sakethh.jetspacer.screens.space.rovers.spirit.SpiritCamerasVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FHAZSpiritCameraScreen() {
    val spiritVM: SpiritCamerasVM = viewModel()
    val roversScreenVM: RoversScreenVM = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val solImagesData = spiritVM.fhazDataFromAPI.value
    LaunchedEffect(key1 = true) {
        spiritVM.retrieveSpiritCameraData(
            cameraName = SpiritCamerasVM.SpiritCameras.FHAZ,
            sol = FHAZSpiritCameraScreen.solValue.value.toInt(),
            page = 0
        )
    }
    Scaffold(floatingActionButtonPosition = FabPosition.Center, floatingActionButton = {
        if (solImagesData.isNotEmpty() && spiritVM._fhazDataFromAPI.value.isEmpty() && spiritVM.isFHAZDataLoaded.value && roversScreenVM.atLastIndexInLazyVerticalGrid.value) {
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
            SolTextField(solValue = FHAZSpiritCameraScreen.solValue, onContinueClick = {
                FHAZSpiritCameraScreen.currentPage = 0
                spiritVM.isFHAZDataLoaded.value = false
                spiritVM.clearSpiritCameraData(cameraName = SpiritCamerasVM.SpiritCameras.FHAZ)
                coroutineScope.launch {
                    spiritVM.retrieveSpiritCameraData(
                        cameraName = SpiritCamerasVM.SpiritCameras.FHAZ,
                        sol = FHAZSpiritCameraScreen.solValue.value.toInt(),
                        page = 0
                    )
                }
            })
            if (!spiritVM.isFHAZDataLoaded.value) {
                StatusScreen(
                    title = "Wait a moment!",
                    description = "fetching the images from this camera that were captured on sol ${FHAZSpiritCameraScreen.solValue.value}",
                    status = Status.LOADING
                )

            } else if (solImagesData.isEmpty()) {
                StatusScreen(
                    title = "4ooooFour",
                    description = "No images were captured by this camera on sol ${FHAZSpiritCameraScreen.solValue.value}. Change the sol value; it may give results.",
                    status = Status.FOURO4InMarsScreen
                )
            } else {
                ModifiedLazyVerticalGrid(
                    listData = solImagesData,
                    loadMoreButtonBooleanExpression = spiritVM._fhazDataFromAPI.value.isNotEmpty() && spiritVM.isFHAZDataLoaded.value
                ) {
                    coroutineScope.launch {
                        spiritVM.retrieveSpiritCameraData(
                            cameraName = SpiritCamerasVM.SpiritCameras.FHAZ,
                            sol = FHAZSpiritCameraScreen.solValue.value.toInt(),
                            page = FHAZSpiritCameraScreen.currentPage++
                        )
                    }
                }
            }

        }
    }
}

object FHAZSpiritCameraScreen {
    var solValue = mutableStateOf("0")
    var currentPage = 0
}