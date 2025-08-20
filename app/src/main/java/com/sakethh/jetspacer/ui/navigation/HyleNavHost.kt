package com.sakethh.jetspacer.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sakethh.jetspacer.domain.model.rover_latest_images.LatestPhoto
import com.sakethh.jetspacer.ui.LocalNavController
import com.sakethh.jetspacer.ui.screens.explore.ExploreScreen
import com.sakethh.jetspacer.ui.screens.explore.apodArchive.APODArchiveScreen
import com.sakethh.jetspacer.ui.screens.explore.apodArchive.APODDetailScreen
import com.sakethh.jetspacer.ui.screens.explore.marsGallery.MarsGalleryScreen
import com.sakethh.jetspacer.ui.screens.explore.marsGallery.RoverImageDetailsScreen
import com.sakethh.jetspacer.ui.screens.explore.search.SearchResultScreen
import com.sakethh.jetspacer.ui.screens.home.HeadlineDetailScreen
import com.sakethh.jetspacer.ui.screens.home.HomeScreen
import com.sakethh.jetspacer.ui.screens.home.state.apod.ModifiedAPODDTO
import com.sakethh.jetspacer.ui.screens.settings.SettingsScreen
import com.sakethh.jetspacer.ui.utils.rememberSerializableObject
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HyleNavHost() {
    val navController = LocalNavController.current
    SharedTransitionLayout {
        NavHost(
            navController = navController, startDestination = HyleNavigation.Root.Home
        ) {
            composable<HyleNavigation.Root.Home> {
                HomeScreen(
                    animatedVisibilityScope = this
                )
            }
            composable<HyleNavigation.Root.Settings> {
                SettingsScreen()
            }
            composable<HyleNavigation.Headlines.TopHeadlineDetailScreen> { navBackStackEntry ->
                HeadlineDetailScreen(
                    animatedVisibilityScope = this,
                    encodedString = navBackStackEntry.toRoute<HyleNavigation.Headlines.TopHeadlineDetailScreen>().encodedString
                )
            }
            composable<HyleNavigation.Root.Explore> {
                ExploreScreen(animatedVisibilityScope = this)
            }
            composable<HyleNavigation.Explore.SearchResultScreen> {
                SearchResultScreen(
                    animatedVisibilityScope = this,
                    searchResult = Json.decodeFromString(it.toRoute<HyleNavigation.Explore.SearchResultScreen>().encodedString)
                )
            }
            composable<HyleNavigation.Explore.APODArchiveScreen> {
                APODArchiveScreen(animatedVisibilityScope = this)
            }
            composable<HyleNavigation.Explore.MarsGalleryScreen> {
                MarsGalleryScreen(animatedVisibilityScope = this)
            }
            composable<HyleNavigation.Latest.Settings> {
                SettingsScreen()
            }
            composable<HyleNavigation.APODArchiveScreen.APODDetailScreen> {
                val apod =
                    rememberSerializableObject(it.toRoute<HyleNavigation.APODArchiveScreen.APODDetailScreen>().apod) {
                        Json.decodeFromString<ModifiedAPODDTO>(it.toRoute<HyleNavigation.APODArchiveScreen.APODDetailScreen>().apod)
                    }
                APODDetailScreen(animatedVisibilityScope = this, apod = apod)
            }
            composable<HyleNavigation.MarsGalleryScreen.RoverImageDetailsScreen> {
                val image =
                    rememberSerializableObject(it.toRoute<HyleNavigation.MarsGalleryScreen.RoverImageDetailsScreen>().image) {
                        Json.decodeFromString<LatestPhoto>(it.toRoute<HyleNavigation.MarsGalleryScreen.RoverImageDetailsScreen>().image)
                    }
                RoverImageDetailsScreen(animatedVisibilityScope = this, image = image)
            }
        }
    }
}