package com.sakethh.jetspacer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sakethh.jetspacer.ui.LocalNavController
import com.sakethh.jetspacer.ui.screens.collection.CollectionsScreen
import com.sakethh.jetspacer.ui.screens.explore.ExploreScreen
import com.sakethh.jetspacer.ui.screens.explore.apodArchive.APODArchiveScreen
import com.sakethh.jetspacer.ui.screens.explore.marsGallery.MarsGalleryScreen
import com.sakethh.jetspacer.ui.screens.explore.search.SearchResultScreen
import com.sakethh.jetspacer.ui.screens.headlines.TopHeadlineDetailScreen
import com.sakethh.jetspacer.ui.screens.headlines.TopHeadlinesScreen
import com.sakethh.jetspacer.ui.screens.home.HomeScreen
import com.sakethh.jetspacer.ui.screens.home.settings.SettingsScreen

@Composable
fun HyleContent() {
    val navController = LocalNavController.current
    NavHost(
        navController = navController, startDestination = HyleNavigation.Root.Latest
    ) {
        composable<HyleNavigation.Root.Latest> {
            HomeScreen()
        }
        composable<HyleNavigation.Root.Settings> {
            SettingsScreen()
        }
        composable<HyleNavigation.Headlines.TopHeadlineDetailScreenRoute> { navBackStackEntry ->
            TopHeadlineDetailScreen(
                navBackStackEntry.toRoute<HyleNavigation.Headlines.TopHeadlineDetailScreenRoute>().encodedString
            )
        }
        composable<HyleNavigation.Root.Explore> {
            ExploreScreen()
        }
        composable<HyleNavigation.Explore.SearchResultScreenRoute> {
            SearchResultScreen(
                it.toRoute<HyleNavigation.Explore.SearchResultScreenRoute>().encodedString
            )
        }
        composable<HyleNavigation.Explore.APODArchiveScreen> {
            APODArchiveScreen(navController)
        }
        composable<HyleNavigation.Explore.MarsGalleryRoute> {
            MarsGalleryScreen(navController)
        }
        composable<HyleNavigation.Latest.Settings> {
            SettingsScreen()
        }
        composable<HyleNavigation.Root.Collections> {
            CollectionsScreen(navController)
        }
    }
}