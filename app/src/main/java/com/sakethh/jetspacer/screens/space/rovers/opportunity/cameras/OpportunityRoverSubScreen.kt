package com.sakethh.jetspacer.screens.space.rovers.opportunity.cameras

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.ModifiedLazyVerticalGrid
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.SolTextField
import com.sakethh.jetspacer.screens.space.rovers.opportunity.OpportunityCamerasVM
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OpportunityRoverSubScreen(cameraName: OpportunityCamerasVM.OpportunityCameras) {
    val opportunityVM: OpportunityCamerasVM = viewModel()
    val roversScreenVM: RoversScreenVM = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val solImagesData = when (cameraName) {
        OpportunityCamerasVM.OpportunityCameras.MINITES -> opportunityVM.minitesDataFromAPI.value
        OpportunityCamerasVM.OpportunityCameras.RHAZ -> opportunityVM.rhazDataFromAPI.value
        OpportunityCamerasVM.OpportunityCameras.FHAZ -> opportunityVM.fhazDataFromAPI.value
        OpportunityCamerasVM.OpportunityCameras.PANCAM -> opportunityVM.pancamDataFromAPI.value
        OpportunityCamerasVM.OpportunityCameras.NAVCAM -> opportunityVM.navcamDataFromAPI.value
        OpportunityCamerasVM.OpportunityCameras.RANDOM -> opportunityVM.randomCameraDataFromAPI.value
    }
    val isDataLoaded = when (cameraName) {
        OpportunityCamerasVM.OpportunityCameras.MINITES -> opportunityVM.isMinitesDataLoaded.value
        OpportunityCamerasVM.OpportunityCameras.RHAZ -> opportunityVM.isRHAZCamDataLoaded.value
        OpportunityCamerasVM.OpportunityCameras.FHAZ -> opportunityVM.isFHAZDataLoaded.value
        OpportunityCamerasVM.OpportunityCameras.PANCAM -> opportunityVM.isPancamDataLoaded.value
        OpportunityCamerasVM.OpportunityCameras.NAVCAM -> opportunityVM.isNAVCAMDataLoaded.value
        OpportunityCamerasVM.OpportunityCameras.RANDOM -> opportunityVM.isRandomCamerasDataLoaded.value
    }
    val expressionForShowingSnackBar = when (cameraName) {
        OpportunityCamerasVM.OpportunityCameras.MINITES -> opportunityVM._minitesDataFromAPI.value.isEmpty() && isDataLoaded
        OpportunityCamerasVM.OpportunityCameras.RHAZ -> opportunityVM._rhazDataFromAPI.value.isEmpty() && isDataLoaded
        OpportunityCamerasVM.OpportunityCameras.FHAZ -> opportunityVM._fhazDataFromAPI.value.isEmpty() && isDataLoaded
        OpportunityCamerasVM.OpportunityCameras.PANCAM -> opportunityVM._pancamDataFromAPI.value.isEmpty() && isDataLoaded
        OpportunityCamerasVM.OpportunityCameras.NAVCAM -> opportunityVM._navcamDataFromAPI.value.isEmpty() && isDataLoaded
        OpportunityCamerasVM.OpportunityCameras.RANDOM -> opportunityVM._randomCameraData.value.isEmpty() && isDataLoaded
    }
    val expressionForShowingLoadMoreBtn = when (cameraName) {
        OpportunityCamerasVM.OpportunityCameras.MINITES -> opportunityVM._minitesDataFromAPI.value.isNotEmpty() && isDataLoaded
        OpportunityCamerasVM.OpportunityCameras.RHAZ -> opportunityVM._rhazDataFromAPI.value.isNotEmpty() && isDataLoaded
        OpportunityCamerasVM.OpportunityCameras.FHAZ -> opportunityVM._fhazDataFromAPI.value.isNotEmpty() && isDataLoaded
        OpportunityCamerasVM.OpportunityCameras.PANCAM -> opportunityVM._pancamDataFromAPI.value.isNotEmpty() && isDataLoaded
        OpportunityCamerasVM.OpportunityCameras.NAVCAM -> opportunityVM._navcamDataFromAPI.value.isNotEmpty() && isDataLoaded
        OpportunityCamerasVM.OpportunityCameras.RANDOM -> opportunityVM._randomCameraData.value.isNotEmpty() && isDataLoaded
    }
    val currentScreenSolValue = when (cameraName) {
        OpportunityCamerasVM.OpportunityCameras.MINITES -> OpportunityRoverSubScreen.minitesSolValue
        OpportunityCamerasVM.OpportunityCameras.RHAZ -> OpportunityRoverSubScreen.rhazSolValue
        OpportunityCamerasVM.OpportunityCameras.FHAZ -> OpportunityRoverSubScreen.fhazSolValue
        OpportunityCamerasVM.OpportunityCameras.PANCAM -> OpportunityRoverSubScreen.pancamSolValue
        OpportunityCamerasVM.OpportunityCameras.NAVCAM -> OpportunityRoverSubScreen.navcamSolValue
        OpportunityCamerasVM.OpportunityCameras.RANDOM -> OpportunityRoverSubScreen.randomCamSolValue
    }
    LaunchedEffect(key1 = true) {
        when (cameraName) {
            OpportunityCamerasVM.OpportunityCameras.MINITES -> {
                opportunityVM.retrieveOpportunityCameraData(
                    cameraName = OpportunityCamerasVM.OpportunityCameras.MINITES,
                    sol = OpportunityRoverSubScreen.minitesSolValue.value.toInt(),
                    page = 0
                )
            }
            OpportunityCamerasVM.OpportunityCameras.RHAZ -> {
                opportunityVM.retrieveOpportunityCameraData(
                    cameraName = OpportunityCamerasVM.OpportunityCameras.RHAZ,
                    sol = OpportunityRoverSubScreen.rhazSolValue.value.toInt(),
                    page = 0
                )
            }
            OpportunityCamerasVM.OpportunityCameras.FHAZ -> {
                opportunityVM.retrieveOpportunityCameraData(
                    cameraName = OpportunityCamerasVM.OpportunityCameras.FHAZ,
                    sol = OpportunityRoverSubScreen.fhazSolValue.value.toInt(),
                    page = 0
                )
            }
            OpportunityCamerasVM.OpportunityCameras.PANCAM -> {
                opportunityVM.retrieveOpportunityCameraData(
                    cameraName = OpportunityCamerasVM.OpportunityCameras.PANCAM,
                    sol = OpportunityRoverSubScreen.pancamSolValue.value.toInt(),
                    page = 0
                )
            }
            OpportunityCamerasVM.OpportunityCameras.NAVCAM -> {
                opportunityVM.retrieveOpportunityCameraData(
                    cameraName = OpportunityCamerasVM.OpportunityCameras.NAVCAM,
                    sol = OpportunityRoverSubScreen.navcamSolValue.value.toInt(),
                    page = 0
                )
            }
            OpportunityCamerasVM.OpportunityCameras.RANDOM -> {
                opportunityVM.retrieveOpportunityCameraData(
                    cameraName = OpportunityCamerasVM.OpportunityCameras.RANDOM,
                    sol = OpportunityRoverSubScreen.randomCamSolValue.value.toInt(),
                    page = 0
                )
            }
        }
    }
    val bottomSheetScaffold = rememberBottomSheetScaffoldState()
    if (solImagesData.isNotEmpty() && expressionForShowingSnackBar && RoversScreenVM.RoverScreenUtils.atLastIndexInLazyVerticalGrid.value) {
        coroutineScope.launch {
            bottomSheetScaffold.bottomSheetState.expand()
            if (bottomSheetScaffold.bottomSheetState.isCollapsed) {
                bottomSheetScaffold.bottomSheetState.expand()
            }
        }
    } else {
        coroutineScope.launch {
            bottomSheetScaffold.bottomSheetState.collapse()
            if (bottomSheetScaffold.bottomSheetState.isExpanded) {
                bottomSheetScaffold.bottomSheetState.collapse()
            }
        }
    }
    val context = LocalContext.current
    fun loadData() {

        OpportunityRoverSubScreen.currentPage = 0
        when (cameraName) {
            OpportunityCamerasVM.OpportunityCameras.MINITES -> {
                opportunityVM.isMinitesDataLoaded.value = false
                opportunityVM.clearOpportunityCameraData(cameraName = OpportunityCamerasVM.OpportunityCameras.MINITES)
                coroutineScope.launch {
                    opportunityVM.retrieveOpportunityCameraData(
                        cameraName = OpportunityCamerasVM.OpportunityCameras.MINITES,
                        sol = OpportunityRoverSubScreen.minitesSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            OpportunityCamerasVM.OpportunityCameras.RHAZ -> {
                opportunityVM.isRHAZCamDataLoaded.value = false
                opportunityVM.clearOpportunityCameraData(cameraName = OpportunityCamerasVM.OpportunityCameras.RHAZ)
                coroutineScope.launch {
                    opportunityVM.retrieveOpportunityCameraData(
                        cameraName = OpportunityCamerasVM.OpportunityCameras.RHAZ,
                        sol = OpportunityRoverSubScreen.rhazSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            OpportunityCamerasVM.OpportunityCameras.FHAZ -> {
                opportunityVM.isFHAZDataLoaded.value = false
                opportunityVM.clearOpportunityCameraData(cameraName = OpportunityCamerasVM.OpportunityCameras.FHAZ)
                coroutineScope.launch {
                    opportunityVM.retrieveOpportunityCameraData(
                        cameraName = OpportunityCamerasVM.OpportunityCameras.FHAZ,
                        sol = OpportunityRoverSubScreen.fhazSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            OpportunityCamerasVM.OpportunityCameras.PANCAM -> {
                opportunityVM.isPancamDataLoaded.value = false
                opportunityVM.clearOpportunityCameraData(cameraName = OpportunityCamerasVM.OpportunityCameras.PANCAM)
                coroutineScope.launch {
                    opportunityVM.retrieveOpportunityCameraData(
                        cameraName = OpportunityCamerasVM.OpportunityCameras.PANCAM,
                        sol = OpportunityRoverSubScreen.pancamSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            OpportunityCamerasVM.OpportunityCameras.NAVCAM -> {
                opportunityVM.isNAVCAMDataLoaded.value = false
                opportunityVM.clearOpportunityCameraData(cameraName = OpportunityCamerasVM.OpportunityCameras.NAVCAM)
                coroutineScope.launch {
                    opportunityVM.retrieveOpportunityCameraData(
                        cameraName = OpportunityCamerasVM.OpportunityCameras.NAVCAM,
                        sol = OpportunityRoverSubScreen.navcamSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
            OpportunityCamerasVM.OpportunityCameras.RANDOM -> {
                opportunityVM.isRandomCamerasDataLoaded.value = false
                opportunityVM.clearOpportunityCameraData(cameraName = OpportunityCamerasVM.OpportunityCameras.RANDOM)
                coroutineScope.launch {
                    opportunityVM.retrieveOpportunityCameraData(
                        cameraName = OpportunityCamerasVM.OpportunityCameras.RANDOM,
                        sol = OpportunityRoverSubScreen.randomCamSolValue.value.toInt(),
                        page = 0
                    )
                }
            }
        }
    }

    val isRefreshing = remember { mutableStateOf(false) }
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing.value,
            onRefresh = {
                isRefreshing.value = true
                Toast.makeText(
                    context,
                    "Refreshing data in a moment",
                    Toast.LENGTH_SHORT
                ).show()
                coroutineScope.launch {
                    loadData()
                }
                if (solImagesData.isNotEmpty()) {
                    isRefreshing.value = false
                }
            })
    Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
        BottomSheetScaffold(scaffoldState = bottomSheetScaffold,
            sheetGesturesEnabled = false,
            drawerBackgroundColor = Color.Transparent,
            drawerContentColor = Color.Transparent,
            drawerScrimColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            sheetBackgroundColor = Color.Transparent,
            sheetPeekHeight = 0.dp,sheetContent = {
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
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            SolTextField(solValue = currentScreenSolValue, onContinueClick = { try {
                loadData()
            }catch (_:Exception){
                if(currentScreenSolValue.value.isEmpty()){
                    Toast.makeText(context,"Value of sol cannot be empty",Toast.LENGTH_SHORT).show()
                }
            } })
            val statusDescriptionForLoadingScreen =
                if (cameraName != OpportunityCamerasVM.OpportunityCameras.RANDOM) {
                    "fetching the images from this camera that were captured on sol ${currentScreenSolValue.value}"
                } else {
                    "fetching the images that were captured on sol ${currentScreenSolValue.value}"
                }
            val statusDescriptionFor404Screen =
                if (cameraName != OpportunityCamerasVM.OpportunityCameras.RANDOM) {
                    "No images were captured by this camera on sol ${currentScreenSolValue.value}. Change the sol value; it may give results"
                } else {
                    "No images were captured on sol ${currentScreenSolValue.value}. Change the sol value; it may give results."
                }
            if (!isDataLoaded) {
                StatusScreen(
                    title = "Wait a moment!",
                    description = statusDescriptionForLoadingScreen,
                    status = Status.LOADING
                )

            } else if (solImagesData.isEmpty()) {
                StatusScreen(
                    title = "4ooooFour",
                    description = statusDescriptionFor404Screen,
                    status = Status.FOURO4InMarsScreen
                )
            } else {
                    ModifiedLazyVerticalGrid(
                        listData = solImagesData,
                        loadMoreButtonBooleanExpression = expressionForShowingLoadMoreBtn
                    ) {
                        when (cameraName) {
                            OpportunityCamerasVM.OpportunityCameras.MINITES -> {
                                coroutineScope.launch {
                                    opportunityVM.retrieveOpportunityCameraData(
                                        cameraName = OpportunityCamerasVM.OpportunityCameras.MINITES,
                                        sol = OpportunityRoverSubScreen.minitesSolValue.value.toInt(),
                                        page = OpportunityRoverSubScreen.currentPage++
                                    )
                                }
                            }
                            OpportunityCamerasVM.OpportunityCameras.RHAZ -> {
                                coroutineScope.launch {
                                    opportunityVM.retrieveOpportunityCameraData(
                                        cameraName = OpportunityCamerasVM.OpportunityCameras.RHAZ,
                                        sol = OpportunityRoverSubScreen.rhazSolValue.value.toInt(),
                                        page = OpportunityRoverSubScreen.currentPage++
                                    )
                                }
                            }
                            OpportunityCamerasVM.OpportunityCameras.FHAZ -> {
                                coroutineScope.launch {
                                    opportunityVM.retrieveOpportunityCameraData(
                                        cameraName = OpportunityCamerasVM.OpportunityCameras.FHAZ,
                                        sol = OpportunityRoverSubScreen.fhazSolValue.value.toInt(),
                                        page = OpportunityRoverSubScreen.currentPage++
                                    )
                                }
                            }
                            OpportunityCamerasVM.OpportunityCameras.PANCAM -> {
                                coroutineScope.launch {
                                    opportunityVM.retrieveOpportunityCameraData(
                                        cameraName = OpportunityCamerasVM.OpportunityCameras.PANCAM,
                                        sol = OpportunityRoverSubScreen.pancamSolValue.value.toInt(),
                                        page = OpportunityRoverSubScreen.currentPage++
                                    )
                                }
                            }
                            OpportunityCamerasVM.OpportunityCameras.NAVCAM -> {
                                coroutineScope.launch {
                                    opportunityVM.retrieveOpportunityCameraData(
                                        cameraName = OpportunityCamerasVM.OpportunityCameras.NAVCAM,
                                        sol = OpportunityRoverSubScreen.navcamSolValue.value.toInt(),
                                        page = OpportunityRoverSubScreen.currentPage++
                                    )
                                }
                            }
                            OpportunityCamerasVM.OpportunityCameras.RANDOM -> {
                                coroutineScope.launch {
                                    opportunityVM.retrieveOpportunityCameraData(
                                        cameraName = OpportunityCamerasVM.OpportunityCameras.RANDOM,
                                        sol = OpportunityRoverSubScreen.randomCamSolValue.value.toInt(),
                                        page = OpportunityRoverSubScreen.currentPage++
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing.value,
            state = pullRefreshState,
            Modifier.align(Alignment.TopCenter),
            scale = true
        )
    }
}

object OpportunityRoverSubScreen {
    var randomCamSolValue = mutableStateOf("0")
    var fhazSolValue = mutableStateOf("0")
    var rhazSolValue = mutableStateOf("0")
    var pancamSolValue = mutableStateOf("0")
    var minitesSolValue = mutableStateOf("0")
    var navcamSolValue = mutableStateOf("0")
    var currentPage = 0
}