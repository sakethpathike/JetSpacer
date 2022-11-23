package com.sakethh.jetspacer.screens.space.apod

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.screens.Coil_Image
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import com.sakethh.jetspacer.screens.navigation.NavigationRoutes
import com.sakethh.jetspacer.ui.theme.AppTheme
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.screens.space.apod.remote.data.APODPaginationFetching
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLifecycleComposeApi::class
)
@Composable
fun APODScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val apodScreenVM: APODScreenVM = viewModel()
    val apodPaginatedData = apodScreenVM.dataForPagination.collectAsStateWithLifecycle().value
    val coroutineScope = rememberCoroutineScope()
    BackHandler {
        navController.navigate(NavigationRoutes.SPACE_SCREEN) {
            popUpTo(0)
        }
    }
    AppTheme {
        Scaffold(topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = "\"Astronomy Picture Of The Day\" Archive\uD83D\uDCF8",
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
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                itemsIndexed(apodPaginatedData) { currentAPODItemIndex: Int, apodItem: APOD_DTO ->
                    if (currentAPODItemIndex == apodPaginatedData.lastIndex) {
                        coroutineScope.launch {
                            apodScreenVM.fetchAPODData()
                        }
                    }
                    Coil_Image().CoilImage(
                        imgURL = apodItem.url.toString(),
                        contentDescription = apodItem.title.toString(),
                        modifier = Modifier,
                        onError = painterResource(id = R.drawable.satellite_filled)
                    )
                }
            }
        }
    }
}