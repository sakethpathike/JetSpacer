package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.screens.home.APODSideIconButton
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.space.apod.APODScreenVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class, ExperimentalLifecycleComposeApi::class
)
@Composable
fun RandomCuriosityCameraScreen() {
    val coroutineScope = rememberCoroutineScope()
    val randomCuriosityCameraVM: RandomCuriosityCameraVM = viewModel()
    val randomCuriosityCameraData =
        randomCuriosityCameraVM.randomCuriosityCameraData.collectAsStateWithLifecycle()
    val atLastIndex = rememberSaveable { mutableStateOf(false) }
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    AppTheme {
        ModalBottomSheetLayout(
            sheetContent = {
                SolTextField()
            },
            sheetState = bottomSheetState,
            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            sheetBackgroundColor = MaterialTheme.colorScheme.primary
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                itemsIndexed(randomCuriosityCameraData.value) { itemIndex: Int, randomCuriosityCameraDataItem: Photo ->
                    atLastIndex.value = itemIndex == randomCuriosityCameraData.value.lastIndex
                    Coil_Image().CoilImage(
                        imgURL = randomCuriosityCameraDataItem.img_src,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(1.dp)
                            .clickable {
                                coroutineScope.launch {
                                    bottomSheetState.show()
                                }
                            },
                        onError = painterResource(id = R.drawable.satellite_filled),
                        contentScale = ContentScale.Crop
                    )
                }


                item {
                    if (atLastIndex.value) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            OutlinedButton(
                                onClick = {
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
                            modifier = Modifier.size(10.dp),
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolTextField() {
    val randomCuriosityCameraVM: RandomCuriosityCameraVM = viewModel()
    val isEditedIconClicked = rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
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
                        text = "value of Sol should be >= 0",
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
                iconBtnModifier = Modifier.padding(top = 15.dp).background(
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
                    iconBtnModifier = Modifier.padding(top = 15.dp).background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.onPrimary.copy(0.3f)
                    )
                )
            }

        }
    }
}