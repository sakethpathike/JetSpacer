package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.Constants
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.localDB.DBImplementation
import com.sakethh.jetspacer.localDB.MarsRoversDBDTO
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.bookMarks.screens.triggerHapticFeedback
import com.sakethh.jetspacer.screens.home.*
import com.sakethh.jetspacer.screens.space.rovers.RoversScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.CuriosityCamerasVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.ManifestForCuriosityVM
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(
    ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ModifiedLazyVerticalGrid(
    listData: List<Photo>,
    loadMoreButtonBooleanExpression: Boolean,
    onLoadMoreImagesBtnPress: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val roversScreenVM: RoversScreenVM = viewModel()
    val curiosityCameraVM: CuriosityCamerasVM = viewModel()
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val atEndOfTheList by remember {
        derivedStateOf {
            lazyStaggeredGridState.atLastIndex()
        }
    }
    LaunchedEffect(key1 = atEndOfTheList) {
        if (atEndOfTheList) {
            RoversScreenVM.RoverScreenUtils.atLastIndexInLazyVerticalGrid.value = true
            RoversScreenVM.RoverScreenUtils.atNearlyLastImageAtLastSolPage.value = true
        } else {
            RoversScreenVM.RoverScreenUtils.atLastIndexInLazyVerticalGrid.value = false
            RoversScreenVM.RoverScreenUtils.atNearlyLastImageAtLastSolPage.value = false
        }
    }
    AppTheme {
        LazyVerticalStaggeredGrid(
            state = lazyStaggeredGridState,
            columns = StaggeredGridCells.Adaptive(150.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            itemsIndexed(listData) { itemIndex: Int, dataItem: Photo ->
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
                if (loadMoreButtonBooleanExpression) {
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
                }/* else if (roversScreenVM.atLastIndexInLazyVerticalGrid.value && curiosityCameraVM._fhazDataFromAPI.value.isEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(50.dp),
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }*/
                Spacer(modifier = Modifier.height(125.dp))
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
fun LazyStaggeredGridState.atLastIndex(): Boolean {
    return this.layoutInfo.visibleItemsInfo.lastOrNull()?.index == this.layoutInfo.totalItemsCount - 1
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolTextField(onContinueClick: () -> Unit, solValue: MutableState<String>) {
    val context= LocalContext.current
    val randomCuriosityCameraVM: RandomCuriosityCameraVM = viewModel()
    val manifestForCuriosityVM: ManifestForCuriosityVM = viewModel()
    val isEditedIconClicked = rememberSaveable { mutableStateOf(false) }
    val supportingText =
        rememberSaveable(manifestForCuriosityVM.maxCuriositySol.value) { mutableStateOf("value of Sol should be >= 0 and <= ${manifestForCuriosityVM.maxCuriositySol.value}") }
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
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary
                ),
                singleLine = true,
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(0.65f),
                value = solValue.value,
                onValueChange = {
                    solValue.value = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    isEditedIconClicked.value = false
                    randomCuriosityCameraVM.currentPage.value = 1
                    if(solValue.value==""){
                        Toast.makeText(
                            context,
                            "Value of sol cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        isEditedIconClicked.value = false
                        randomCuriosityCameraVM.currentPage.value = 1
                        onContinueClick()
                    }
                }),
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
                        if(solValue.value==""){
                            Toast.makeText(
                                context,
                                "Value of sol cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                            isEditedIconClicked.value = false
                            randomCuriosityCameraVM.currentPage.value = 1
                            onContinueClick()
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
    cameraName: String,
    sol: String,
    earthDate: String,
    roverName: String,
    roverStatus: String,
    launchingDate: String,
    landingDate: String,
    onBookMarkButtonClick: () -> Unit,
    onConfirmBookMarkDeletionButtonClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val bookMarksVM: BookMarksVM = viewModel()
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    bookMarksVM.doesThisExistsInRoverDBIconTxt(imgURL)
    val context = LocalContext.current
    var didDataAddedToDB = false

    LazyColumn {
        item {
            ConstraintLayout(constraintSet = constraintSet) {
                APODMediaLayout(
                    homeScreenViewModel = homeScreenViewModel,
                    imageURL = imgURL,
                    apodMediaType = "image",
                    inAPODBottomSheetContent = false,
                    onBookMarkButtonClick = {
                        triggerHapticFeedback(context = context)
                        onBookMarkButtonClick()
                        bookMarksVM.doesThisExistsInRoverDBIconTxt(imgURL)
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
    bookMarksVM.doesThisExistsInRoverDBIconTxt(imageURL = imgURL)
    if (HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value || HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value) {
        AlertDialogForDeletingFromDB(
            bookMarkedCategory = Constants.SAVED_IN_APOD_DB,
            onConfirmBtnClick = {
                triggerHapticFeedback(context = context)
                onConfirmBookMarkDeletionButtonClick()
                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB.value = false
                HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB.value = false
            }
        )
    }
    bookMarksVM.doesThisExistsInRoverDBIconTxt(imgURL)
}