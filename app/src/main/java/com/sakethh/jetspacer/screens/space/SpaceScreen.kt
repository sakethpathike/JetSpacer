package com.sakethh.jetspacer.screens.space

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sakethh.jetspacer.screens.home.CardForRowGridRaw
import com.sakethh.jetspacer.screens.home.CardRowGrid
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
    AppTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            item {
                CardForRowGridRaw(
                    title = "APOD",
                    value = "Browse \"Astronomy Picture Of The Day\" Archive",
                    cardModifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .clickable { navController.navigate(NavigationRoutes.APOD_SCREEN)}
                )
                CardForRowGridRaw(
                    title = "Mars Rovers",
                    value = "Browse images captured by \"Mars Rovers\" straight from the Mars\uD83D\uDD0D",
                    cardModifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .clickable { }
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
