package com.sakethh.jetspacer.screens.space.rovers.spirit.cameras.random

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.home.*
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.ModifiedLazyVerticalGrid
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.SolTextField
import com.sakethh.jetspacer.screens.space.rovers.opportunity.OpportunityCamerasVM
import com.sakethh.jetspacer.screens.space.rovers.spirit.SpiritCamerasVM
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")

@Composable
fun RandomSpiritCameraScreen() {
    val spiritVM: SpiritCamerasVM = viewModel()
    val roversScreenVM: RoversScreenVM = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val solImagesData = spiritVM.randomCameraDataFromAPI.value
    LaunchedEffect(key1 = true) {
        spiritVM.retrieveSpiritCameraData(
            cameraName = SpiritCamerasVM.SpiritCameras.RANDOM,
            sol = RandomSpiritCameraScreen.solValue.value.toInt(),
            page = 0
        )
    }
    Scaffold(floatingActionButtonPosition = FabPosition.Center, floatingActionButton = {
        if (solImagesData.isNotEmpty() && spiritVM._randomCameraData.value.isEmpty() && spiritVM.isRandomCamerasDataLoaded.value && roversScreenVM.atLastIndexInLazyVerticalGrid.value) {
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
            SolTextField(solValue = RandomSpiritCameraScreen.solValue, onContinueClick = {
                RandomSpiritCameraScreen.currentPage = 0
                spiritVM.isRandomCamerasDataLoaded.value = false
                spiritVM.clearSpiritCameraData(cameraName = SpiritCamerasVM.SpiritCameras.RANDOM)
                coroutineScope.launch {
                    spiritVM.retrieveSpiritCameraData(
                        cameraName = SpiritCamerasVM.SpiritCameras.RANDOM,
                        sol = RandomSpiritCameraScreen.solValue.value.toInt(),
                        page = 0
                    )
                }
            })
            if (!spiritVM.isRandomCamerasDataLoaded.value) {
                StatusScreen(
                    title = "Wait a moment!",
                    description = "fetching the images that were captured on sol ${RandomSpiritCameraScreen.solValue.value}",
                    status = Status.LOADING
                )

            } else if (solImagesData.isEmpty()) {
                StatusScreen(
                    title = "4ooooFour",
                    description = "No images were captured on sol ${RandomSpiritCameraScreen.solValue.value}. Change the sol value; it may give results.",
                    status = Status.FOURO4InMarsScreen
                )
            } else {
                ModifiedLazyVerticalGrid(
                    listData = solImagesData,
                    loadMoreButtonBooleanExpression = spiritVM._randomCameraData.value.isNotEmpty() && spiritVM.isRandomCamerasDataLoaded.value
                ) {
                    coroutineScope.launch {
                        spiritVM.retrieveSpiritCameraData(
                            cameraName = SpiritCamerasVM.SpiritCameras.RANDOM,
                            sol = RandomSpiritCameraScreen.solValue.value.toInt(),
                            page = RandomSpiritCameraScreen.currentPage++
                        )
                    }
                }
            }

        }
    }
}

object RandomSpiritCameraScreen {
    var solValue = mutableStateOf("0")
    var currentPage = 0
}