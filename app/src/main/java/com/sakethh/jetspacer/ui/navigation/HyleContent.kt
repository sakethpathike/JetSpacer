package com.sakethh.jetspacer.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sakethh.jetspacer.ui.LocalNavController
import com.sakethh.jetspacer.ui.screens.collection.CollectionsScreen
import com.sakethh.jetspacer.ui.screens.explore.ExploreScreen
import com.sakethh.jetspacer.ui.screens.explore.apodArchive.APODArchiveScreen
import com.sakethh.jetspacer.ui.screens.explore.apodArchive.APODDetailScreen
import com.sakethh.jetspacer.ui.screens.explore.marsGallery.MarsGalleryScreen
import com.sakethh.jetspacer.ui.screens.explore.search.SearchResultScreen
import com.sakethh.jetspacer.ui.screens.headlines.TopHeadlineDetailScreen
import com.sakethh.jetspacer.ui.screens.home.HomeScreen
import com.sakethh.jetspacer.ui.screens.home.settings.SettingsScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HyleContent() {
    val navController = LocalNavController.current
    SharedTransitionLayout {
        NavHost(
            navController = navController, startDestination = HyleNavigation.Root.Home
        ) {
            composable<HyleNavigation.Root.Home> {
                HomeScreen()
            }
            composable<HyleNavigation.Root.Settings> {
                SettingsScreen()
            }
            composable<HyleNavigation.Headlines.TopHeadlineDetailScreen> { navBackStackEntry ->
                TopHeadlineDetailScreen(
                    navBackStackEntry.toRoute<HyleNavigation.Headlines.TopHeadlineDetailScreen>().encodedString
                )
            }
            composable<HyleNavigation.Root.Explore> {
                ExploreScreen()
            }
            composable<HyleNavigation.Explore.SearchResultScreen> {
                SearchResultScreen(
                    it.toRoute<HyleNavigation.Explore.SearchResultScreen>().encodedString
                )
            }
            composable<HyleNavigation.Explore.APODArchiveScreen> {
                APODArchiveScreen(animatedVisibilityScope = this)
            }
            composable<HyleNavigation.Explore.MarsGalleryScreen> {
                MarsGalleryScreen(navController)
            }
            composable<HyleNavigation.Latest.Settings> {
                SettingsScreen()
            }
            composable<HyleNavigation.Root.Collections> {
                CollectionsScreen(navController)
            }
            composable<HyleNavigation.APODArchiveScreen.APODDetailScreen> {
                val apodDTO = it.toRoute<HyleNavigation.APODArchiveScreen.APODDetailScreen>()
                APODDetailScreen(animatedVisibilityScope = this, apod = apodDTO.apod)
            }
        }
    }
}