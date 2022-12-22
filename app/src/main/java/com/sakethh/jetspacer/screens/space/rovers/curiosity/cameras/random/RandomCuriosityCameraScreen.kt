package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.localDB.MarsRoversDBDTO
import com.sakethh.jetspacer.screens.home.*
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLifecycleComposeApi::class)
@SuppressLint("CoroutineCreationDuringComposition")

@Composable
fun RandomCuriosityCameraScreen() {
    val coroutineScope = rememberCoroutineScope()
    val randomCuriosityCameraVM: RandomCuriosityCameraVM = viewModel()
    val randomCuriosityCameraData =
        randomCuriosityCameraVM.randomCuriosityCameraData.collectAsStateWithLifecycle().value
    ModifiedLazyVerticalGrid(listData = randomCuriosityCameraData) {
        randomCuriosityCameraVM.currentPage.value =
            randomCuriosityCameraVM.currentPage.value + 1
        coroutineScope.launch {
            randomCuriosityCameraVM.enteredSol.value?.let { it1 ->
                randomCuriosityCameraVM.getRandomCuriosityData(
                    sol = it1.toInt(),
                    page = randomCuriosityCameraVM.currentPage.value
                )
            }
        }
    }
}

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalLifecycleComposeApi::class
)
@Composable
fun ModifiedLazyVerticalGrid(listData: List<Photo>, onLoadMoreImagesBtnPress: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val randomCuriosityCameraVM: RandomCuriosityCameraVM = viewModel()
    val roversScreenVM: RoversScreenVM = viewModel()
    val randomCuriosityCameraData =
        randomCuriosityCameraVM.randomCuriosityCameraData.collectAsStateWithLifecycle()
    val atLastIndex = rememberSaveable { mutableStateOf(false) }
    AppTheme {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(150.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            itemsIndexed(listData) { itemIndex: Int, dataItem: Photo ->
                atLastIndex.value = itemIndex == randomCuriosityCameraData.value.lastIndex
                Coil_Image().CoilImage(
                    imgURL = dataItem.img_src,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(1.dp)
                        .clickable {
                            coroutineScope.launch {
                                roversScreenVM.openRoverBtmSheet(
                                    imgURL = dataItem.img_src,
                                    capturedOn = dataItem.earth_date,
                                    cameraName = dataItem.camera.full_name,
                                    sol = dataItem.sol.toString(),
                                    earthDate = dataItem.earth_date,
                                    roverName = dataItem.rover.name,
                                    roverStatus = dataItem.rover.status,
                                    launchingDate = dataItem.rover.launch_date,
                                    landingDate = dataItem.rover.landing_date
                                )
                            }
                        },
                    onError = painterResource(id = R.drawable.satellite_filled),
                    contentScale = ContentScale.Crop
                )
            }


            item {
                if (atLastIndex.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        OutlinedButton(
                            onClick = {
                                onLoadMoreImagesBtnPress()
                            },
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                text = "Load more images",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(50.dp),
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolTextField() {
    val randomCuriosityCameraVM: RandomCuriosityCameraVM = viewModel()
    val isEditedIconClicked = rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val supportingText = rememberSaveable { mutableStateOf("value of Sol should be >= 0") }
    AppTheme {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .wrapContentHeight()
                .animateContentSize(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    cursorColor = MaterialTheme.colorScheme.inverseSurface
                ),
                singleLine = true,
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(0.65f),
                value = randomCuriosityCameraVM.enteredSol.value.toString(),
                onValueChange = {
                    randomCuriosityCameraVM.enteredSol.value = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                enabled = isEditedIconClicked.value,
                supportingText = {
                    Text(
                        text = supportingText.value,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 15.dp),
                        fontSize = 15.sp
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                maxLines = 1
            )

            APODSideIconButton(
                imageVector = Icons.Outlined.Edit,
                onClick = {
                    isEditedIconClicked.value = true
                },
                iconBtnColor = MaterialTheme.colorScheme.onPrimary.copy(0.3f),
                iconColor = MaterialTheme.colorScheme.onPrimary,
                iconModifier = Modifier,
                iconBtnModifier = Modifier
                    .padding(top = 15.dp)
                    .background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.onPrimary.copy(0.3f)
                    )
            )

            if (isEditedIconClicked.value) {
                APODSideIconButton(
                    imageVector = Icons.Outlined.Done,
                    onClick = {
                        isEditedIconClicked.value = false
                        randomCuriosityCameraVM.currentPage.value = 1
                        coroutineScope.launch {
                            // supportingText.value = "value of Sol can't be empty"
                            randomCuriosityCameraVM.enteredSol.value?.let {
                                randomCuriosityCameraVM.getRandomCuriosityData(
                                    sol = it.toInt(),
                                    1
                                )
                            }
                        }
                    },
                    iconBtnColor = MaterialTheme.colorScheme.onPrimary.copy(0.3f),
                    iconColor = MaterialTheme.colorScheme.onPrimary,
                    iconModifier = Modifier,
                    iconBtnModifier = Modifier
                        .padding(top = 15.dp)
                        .background(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.onPrimary.copy(0.3f)
                        )
                )
            }

        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RoverBottomSheetContent(
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
    val coroutineScope = rememberCoroutineScope()
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    coroutineScope.launch {
        homeScreenViewModel.doesThisExistsInRoverDBIconTxt(imgURL)
    }
    val context = LocalContext.current

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    val currentDate = dateFormat.format(Calendar.getInstance().time)
    LazyColumn {
        item {
            ConstraintLayout(constraintSet = constraintSet) {
                APODMediaLayout(
                    homeScreenViewModel = homeScreenViewModel,
                    imageURL = imgURL,
                    apodTitle = "",
                    apodDate = capturedOn,
                    apodDescription = "",
                    saveToMarsRoversDB = true,
                    apodMediaType = "image",
                    saveToAPODDB = false,
                    inAPODBottomSheetContent = false,
                    marsRoversDBDTO = MarsRoversDBDTO().apply {
                        this.addedToLocalDBOn = currentDate
                        this.capturedBy = cameraName
                        this.category = Constants.SAVED_IN_ROVERS_DB
                        this.earthDate = earthDate
                        this.roverStatus = roverStatus
                        this.roverName = roverName
                        this.id = imgURL
                        this.imageURL = imgURL
                        this.isBookMarked = true
                        this.landingDate = landingDate
                        this.launchingDate = launchingDate
                        this.sol = sol
                    }
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(0.dp))
        }
        item {
            CardForRowGridRaw(
                title = "Captured by",
                value = cameraName,
                cardModifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(15.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(0.dp))
        }
        item {
            Divider(
                thickness = 0.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(
                    start = 25.dp,
                    end = 25.dp,
                    top = 0.dp,
                    bottom = 10.dp
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(1.dp))
        }
        item {
            CardRowGrid(
                lhsCardTitle = "Sol",
                lhsCardValue = sol,
                rhsCardTitle = "Earth Date",
                rhsCardValue = earthDate
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            CardRowGrid(
                lhsCardTitle = "Rover Name",
                lhsCardValue = roverName,
                rhsCardTitle = "Rover Status",
                rhsCardValue = roverStatus
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            CardRowGrid(
                lhsCardTitle = "Launching Date",
                lhsCardValue = launchingDate,
                rhsCardTitle = "Landing Date",
                rhsCardValue = landingDate
            )
        }

        item {
            Divider(
                thickness = 0.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(
                    start = 25.dp,
                    end = 25.dp,
                    top = 15.dp,
                    bottom = 75.dp
                )
            )
        }
    }
    coroutineScope.launch {
        homeScreenViewModel.doesThisExistsInAPODIconTxt(imageURL = imgURL)
    }
}