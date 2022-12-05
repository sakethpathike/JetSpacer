package com.sakethh.jetspacer.screens.space.apod

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.ui.theme.AppTheme
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.localDB.APOD_DB_DTO
import com.sakethh.jetspacer.screens.home.*
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLifecycleComposeApi::class, ExperimentalMaterialApi::class
)
@Composable
fun APODScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val apodScreenVM: APODScreenVM = viewModel()
    val apodPaginatedData = apodScreenVM.dataForPagination.collectAsStateWithLifecycle().value
    val coroutineScope = rememberCoroutineScope()

    val apodURL = rememberSaveable { mutableStateOf("") }
    val apodTitle = rememberSaveable { mutableStateOf("") }
    val apodDate = rememberSaveable { mutableStateOf("") }
    val apodDescription = rememberSaveable { mutableStateOf("") }
    val apodMediaType = rememberSaveable { mutableStateOf("") }
    val homeScreenVM: HomeScreenViewModel = viewModel()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    BackHandler {
        if (bottomSheetState.isVisible) {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        } else {
            navController.navigate(NavigationRoutes.SPACE_SCREEN) {
                popUpTo(0)
            }
        }
    }
    AppTheme {
        Scaffold(topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = "APOD Archive",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 22.sp,
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }) {

            ModalBottomSheetLayout(
                sheetContent = {
                    APODBottomSheetContent(
                        homeScreenViewModel = homeScreenVM,
                        apodURL = apodURL.value,
                        apodTitle = apodTitle.value,
                        apodDate = apodDate.value,
                        apodDescription = apodDescription.value,
                        apodMediaType = apodMediaType.value
                    )
                },
                sheetState = bottomSheetState,
                sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                sheetBackgroundColor = MaterialTheme.colorScheme.primary
            ) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(150.dp),
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .padding(it)
                ) {
                    itemsIndexed(apodPaginatedData) { currentAPODItemIndex: Int, apodItem: APOD_DTO ->
                        if (currentAPODItemIndex == apodPaginatedData.lastIndex - 6) {
                            coroutineScope.launch {
                                apodScreenVM.fetchAPODData()
                            }
                        }
                        Coil_Image().CoilImage(
                            imgURL = apodItem.url.toString(),
                            contentDescription = apodItem.title.toString(),
                            modifier = Modifier
                                .padding(1.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        apodDate.value = apodItem.date.toString()
                                        apodURL.value = apodItem.url.toString()
                                        apodDescription.value = apodItem.explanation.toString()
                                        apodTitle.value = apodItem.title.toString()
                                        apodMediaType.value = apodItem.media_type.toString()
                                        bottomSheetState.show()
                                    }
                                },
                            onError = painterResource(id = R.drawable.satellite_filled)
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(25.dp))
                    }
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun APODBottomSheetContent(
    homeScreenViewModel: HomeScreenViewModel,
    apodURL: String,
    apodTitle: String,
    apodDate: String,
    apodDescription: String,
    apodMediaType: String,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    coroutineScope.launch {
        homeScreenViewModel.doesThisExistsInAPODIconTxt(apodURL)
    }
    LazyColumn {
        item {
            ConstraintLayout(constraintSet = constraintSet) {
                APODMediaLayout(
                    homeScreenViewModel = homeScreenViewModel,
                    apodURL = apodURL,
                    apodTitle = apodTitle,
                    apodDate = apodDate,
                    apodDescription = apodDescription,
                    inAPODScreen = true,
                    apodMediaType = apodMediaType
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CardForRowGridRaw(
                    title = "Published on",
                    value = apodDate,
                    cardModifier = Modifier
                        .wrapContentHeight()
                        .width(150.dp)
                        .align(CenterVertically)
                )
                Column(
                    modifier = Modifier.height(85.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    APODBottomSheetChip(
                        onClick = {
                            coroutineScope.launch {
                                homeScreenViewModel.dbImplementation.addNewBookMarkToAPODDB(
                                    APOD_DB_DTO().apply {
                                        id = apodURL
                                        title = apodTitle
                                        datePublished = apodDate
                                        description = apodDescription
                                        imageURL = apodURL
                                        this.mediaType=apodMediaType
                                        isBookMarked = true
                                    }
                                )
                            }
                            homeScreenViewModel.doesThisExistsInAPODIconTxt(apodURL)
                            if (!homeScreenViewModel.dbUtils.doesThisExistsInDBAPOD(apodURL)) {
                                Toast.makeText(
                                    context,
                                    "Added to bookmarks:)",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        imageVector = homeScreenViewModel.bookMarkIcons.value,
                        iconColor = MaterialTheme.colorScheme.onPrimary,
                        text = "Bookmark",
                        textColor = MaterialTheme.colorScheme.onPrimary
                    )
                    APODBottomSheetChip(
                        onClick = { },
                        imageVector = Icons.Outlined.FileDownload,
                        iconColor = MaterialTheme.colorScheme.onPrimary,
                        text = "Download",
                        textColor = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        item {
            Divider(
                thickness = 0.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(
                    start = 25.dp,
                    end = 25.dp,
                    top = 15.dp,
                    bottom = 15.dp
                )
            )
        }
        item {
            Text(
                text = "Title : $apodTitle",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                lineHeight = 18.sp,
                textAlign = TextAlign.Start
            )
        }
        item {
            Text(
                text = apodDescription,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                lineHeight = 20.sp,
                textAlign = TextAlign.Start
            )
        }
/*item {
    Text(
        text = "© $apodCopyright",
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 18.sp,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
        lineHeight = 20.sp,
        textAlign = TextAlign.Start
    )
}*/

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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APODBottomSheetChip(
    onClick: () -> Unit, imageVector: ImageVector,
    iconColor: androidx.compose.ui.graphics.Color,
    text: String,
    textColor: Color
) {
    AssistChip(
        onClick = { onClick() },
        leadingIcon = {
            androidx.compose.material3.Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(AssistChipDefaults.IconSize)
            )
        },
        label = {
            androidx.compose.material3.Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        shape = RoundedCornerShape(5.dp),
        colors = AssistChipDefaults.assistChipColors(),
        border = AssistChipDefaults.assistChipBorder(borderWidth = 0.dp, borderColor = textColor)
    )
}