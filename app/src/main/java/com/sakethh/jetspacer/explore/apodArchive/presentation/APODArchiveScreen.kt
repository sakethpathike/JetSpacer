package com.sakethh.jetspacer.explore.apodArchive.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.common.utils.jetSpacerLog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APODArchiveScreen(navController: NavController) {
    val apodArchiveScreenViewModel: APODArchiveScreenViewModel = viewModel()
    val apodArchiveState = apodArchiveScreenViewModel.apodArchiveState.value
    val context = LocalContext.current
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val lazyVerticalStaggeredGridState = rememberLazyStaggeredGridState()
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumTopAppBar(scrollBehavior = topAppBarScrollBehavior, title = {
                Text("APOD Archive", style = MaterialTheme.typography.titleSmall, fontSize = 16.sp)
            }, navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            })
        }) {
        LazyVerticalStaggeredGrid(
            state = lazyVerticalStaggeredGridState,
            columns = StaggeredGridCells.Adaptive(150.dp),
            modifier = Modifier
                .padding(it)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Spacer(Modifier.height(10.dp))
            }
            items(apodArchiveState.data) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(it.url)
                        .crossfade(true).build(),
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(5.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            1.5.dp, LocalContentColor.current.copy(0.25f), RoundedCornerShape(15.dp)
                        ), contentDescription = null
                )
            }
        }
    }

    LaunchedEffect(lazyVerticalStaggeredGridState.canScrollForward) {
        if (lazyVerticalStaggeredGridState.canScrollForward.not() && apodArchiveState.data.isNotEmpty() && !apodArchiveState.isLoading && !apodArchiveState.error) {
            jetSpacerLog("triggering from launched effect")
            apodArchiveScreenViewModel.retrieveNextBatchOfAPODArchive()
        }
    }
}