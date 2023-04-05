package com.sakethh.jetspacer.screens.bookMarks

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.localDB.BookMarkScreenGridNames
import com.sakethh.jetspacer.localDB.SavedDataType
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.bookMarks.screens.APODBookMarksScreen
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.apod.APODBottomSheetContent
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun BookMarksScreen(navController: NavController) {
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        if (bottomSheetState.isVisible) {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        } else {
            navController.navigate(NavigationRoutes.HOME_SCREEN) {
                popUpTo(0)
            }
        }

    }



    val bookMarksVM: BookMarksVM = viewModel()
    val bookMarkGridData =
        BookMarksVM.dbImplementation.localDBData().getCustomBookMarkTopicData().collectAsState(
            initial = listOf(
                BookMarkScreenGridNames(
                    "", "",
                    emptyList(), SavedDataType.ALL
                )
            )
        ).value
    val selectedChipIndex = rememberSaveable { mutableStateOf(0) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    AppTheme {
        Scaffold(modifier = Modifier.background(MaterialTheme.colorScheme.surface), topBar = {
            TopAppBar(scrollBehavior = scrollBehavior, title = {
                Text(
                    text = "Bookmarks",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(top = 30.dp, start = 15.dp)
                )
            })
        }) {
            if (bookMarkGridData.isNotEmpty()) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    columns = GridCells.Adaptive(250.dp)
                ) {
                    items(bookMarkGridData) { element ->
                        Column(
                            modifier = Modifier
                                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
                                .clip(
                                    RoundedCornerShape(5.dp)
                                )
                                .border(
                                    BorderStroke(5.dp, MaterialTheme.colorScheme.onSurface),
                                    RoundedCornerShape(5.dp)
                                )
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Coil_Image().CoilImage(
                                imgURL = element.imgUrlForGrid,
                                contentDescription = "",
                                modifier = Modifier.fillMaxHeight(0.85f),
                                onError = painterResource(id = R.drawable.baseline_image_24)
                            )
                            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onPrimary)
                            Text(
                                text = element.name,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                        }
                    }
                }
            } else {
                StatusScreen(
                    title = "Nothing here!",
                    description = "Bookmark the thing you like; you can visit them later from here!",
                    status = Status.BOOKMARKS_EMPTY
                )
            }
        }
    }
}

/*

    stickyHeader {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(10.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        bookMarksVM.bookMarksScreensData.forEachIndexed { index, bookMarksScreensData ->
                            FilterChip(
                                modifier = Modifier.padding(end = 10.dp),
                                selected = selectedChipIndex.value == index,
                                onClick = {
                                    selectedChipIndex.value = index
                                },
                                label = {
                                    Text(
                                        text = bookMarksScreensData.screenName,
                                        color = if (selectedChipIndex.value == index) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                },
                                border = FilterChipDefaults.filterChipBorder(
                                    selectedBorderColor = MaterialTheme.colorScheme.onSurface,
                                    borderWidth = 1.dp,
                                    borderColor = MaterialTheme.colorScheme.onSurface
                                ),
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.onSurface)
                            )
                        }
                    }
                }

                bookMarksVM.bookMarksScreensData[selectedChipIndex.value].screenComposable(navController)
* */