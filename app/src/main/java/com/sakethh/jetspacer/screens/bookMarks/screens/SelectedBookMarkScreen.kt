package com.sakethh.jetspacer.screens.bookMarks.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.localDB.*
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedBookMarkScreen(navController: NavController) {
    BackHandler {
        navController.navigate(NavigationRoutes.BOOKMARKS_SCREEN) {
            popUpTo(0)
        }
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val selectedBookMarkScreenVM: SelectedBookMarkScreenVM = viewModel()
    val selectedData =
        selectedBookMarkScreenVM.selectedBookMarkData.collectAsState().value
    val bookMarksVM: BookMarksVM = viewModel()
    val apodData =
        bookMarksVM.localDB.getBookMarkedAPODDBDATA().collectAsState(initial = emptyList()).value
    val newsData =
        bookMarksVM.localDB.getBookMarkedNewsDATA().collectAsState(initial = emptyList()).value
    val roversData =
        bookMarksVM.localDB.getBookMarkedRoverDBDATA().collectAsState(initial = emptyList()).value
    AppTheme {
        Scaffold(modifier = Modifier.background(MaterialTheme.colorScheme.surface), topBar = {
            TopAppBar(scrollBehavior = scrollBehavior, title = {
                Text(
                    text = selectedData.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, end = 15.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            })
        }) {
            LazyVerticalGrid(
                modifier = Modifier.padding(it),
                columns = GridCells.Adaptive(120.dp)
            ) {
                when (selectedData.name) {
                    "APOD" -> {
                        items(apodData){element->
                            Coil_Image().CoilImage(
                                imgURL = element.imageURL,
                                contentDescription = "",
                                modifier = Modifier.padding(1.dp).height(120.dp),
                                onError = painterResource(id = R.drawable.baseline_image_24),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    "News" -> {
                        items(newsData){element->
                            Coil_Image().CoilImage(
                                imgURL = element.imageURL,
                                contentDescription = "",
                                modifier = Modifier.height(120.dp),
                                onError = painterResource(id = R.drawable.baseline_image_24),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    "Rovers" -> {
                        items(roversData){element->
                            Coil_Image().CoilImage(
                                imgURL = element.imageURL,
                                contentDescription = "",
                                modifier = Modifier.height(120.dp),
                                onError = painterResource(id = R.drawable.baseline_image_24),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    else -> {
                        itemsIndexed(selectedData.data) { index, element ->
                            val imgURL: String =
                                when (selectedData.data[index].dataType) {
                                    SavedDataType.APOD -> {
                                        element.data as APOD_DB_DTO
                                        element.data.imageURL
                                    }

                                    SavedDataType.ROVERS -> {
                                        element.data as MarsRoversDBDTO
                                        element.data.imageURL
                                    }
                                    SavedDataType.NEWS -> {
                                        element.data as NewsDB
                                        element.data.imageURL
                                    }
                                    else -> {
                                        ""
                                    }
                                }
                            Coil_Image().CoilImage(
                                imgURL = imgURL,
                                contentDescription = "",
                                modifier = Modifier.height(135.dp),
                                onError = painterResource(id = R.drawable.baseline_image_24),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}