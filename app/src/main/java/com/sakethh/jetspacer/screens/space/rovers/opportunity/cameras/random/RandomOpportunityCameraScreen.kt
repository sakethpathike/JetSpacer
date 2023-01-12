package com.sakethh.jetspacer.screens.space.rovers.opportunity.cameras.random

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
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")

@Composable
fun RandomOpportunityCameraScreen() {
    val opportunityVM: OpportunityCamerasVM = viewModel()
    val roversScreenVM: RoversScreenVM = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val solImagesData = opportunityVM.randomCameraDataFromAPI.value
    LaunchedEffect(key1 = true) {
        opportunityVM.retrieveOpportunityCameraData(
            cameraName = OpportunityCamerasVM.OpportunityCameras.RANDOM,
            sol = RandomOpportunityCameraScreen.solValue.value.toInt(),
            page = 0
        )
    }
    Scaffold(floatingActionButtonPosition = FabPosition.Center, floatingActionButton = {
        if (solImagesData.isNotEmpty() && opportunityVM._randomCameraData.value.isEmpty() && opportunityVM.isRandomCamerasDataLoaded.value && roversScreenVM.atLastIndexInLazyVerticalGrid.value) {
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
            SolTextField(solValue = RandomOpportunityCameraScreen.solValue, onContinueClick = {
                RandomOpportunityCameraScreen.currentPage = 0
                opportunityVM.isRandomCamerasDataLoaded.value = false
                opportunityVM.clearOpportunityCameraData(cameraName = OpportunityCamerasVM.OpportunityCameras.RANDOM)
                coroutineScope.launch {
                    opportunityVM.retrieveOpportunityCameraData(
                        cameraName = OpportunityCamerasVM.OpportunityCameras.RANDOM,
                        sol = RandomOpportunityCameraScreen.solValue.value.toInt(),
                        page = 0
                    )
                }
            })
            if (!opportunityVM.isRandomCamerasDataLoaded.value) {
                StatusScreen(
                    title = "Wait a moment!",
                    description = "fetching the images that were captured on sol ${RandomOpportunityCameraScreen.solValue.value}",
                    status = Status.LOADING
                )

            } else if (solImagesData.isEmpty()) {
                StatusScreen(
                    title = "4ooooFour",
                    description = "No images were captured on sol ${RandomOpportunityCameraScreen.solValue.value}. Change the sol value; it may give results.",
                    status = Status.FOURO4InMarsScreen
                )
            } else {
                ModifiedLazyVerticalGrid(
                    listData = solImagesData,
                    loadMoreButtonBooleanExpression = opportunityVM._randomCameraData.value.isNotEmpty() && opportunityVM.isRandomCamerasDataLoaded.value
                ) {
                    coroutineScope.launch {
                        opportunityVM.retrieveOpportunityCameraData(
                            cameraName = OpportunityCamerasVM.OpportunityCameras.RANDOM,
                            sol = RandomOpportunityCameraScreen.solValue.value.toInt(),
                            page = RandomOpportunityCameraScreen.currentPage++
                        )
                    }
                }
            }

        }
    }
}

object RandomOpportunityCameraScreen {
    var solValue = mutableStateOf("0")
    var currentPage = 0
}