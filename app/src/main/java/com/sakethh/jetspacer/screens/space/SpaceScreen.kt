package com.sakethh.jetspacer.screens.space

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.screens.home.CardForRowGridRaw
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.home.WebViewModified
import com.sakethh.jetspacer.screens.navigation.NavigationRoutes
import com.sakethh.jetspacer.ui.theme.AppTheme

@Composable
fun SpaceScreen(navController: NavController) {
    BackHandler {
        navController.navigate(NavigationRoutes.HOME_SCREEN) {
            popUpTo(0)
        }
    }
    val homeScreenViewModel:HomeScreenViewModel= viewModel()
    val apodImgUrl=homeScreenViewModel.apodDataFromAPI.value.url.toString()
    AppTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            item {
                Row {
                    CardForRowGridRaw(
                        title = "APOD",
                        value = "Browse \"Astronomy Picture Of The Day\" Archive",
                        cardModifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable { navController.navigate(NavigationRoutes.APOD_SCREEN) },
                        inSpaceScreen = true,
                        imgURL = apodImgUrl,
                        imageHeight = 130.dp
                    )
                }
                
                CardForRowGridRaw(
                    title = "Mars Rovers",
                    value = "Browse images captured by \"Mars Rovers\" straight from the Mars",
                    cardModifier = Modifier
                        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable { },
                    inSpaceScreen = true,
                    imgURL = "https://ia601406.us.archive.org/18/items/jetspacer/rover%20original%20flipped%20low%20quality%20jpg.jpg",
                    imageHeight = 130.dp
                )
            }
            item {
                WebViewModified(
                    url = null,
                    embedString = "<iframe width=\"660\" height=\"371\" src=\"https://www.youtube.com/embed/86YLFOog4GM\" title=\"\uD83C\uDF0E Nasa Live Stream  - Earth From Space :  Live Views from the ISS\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }
        }
    }
}
