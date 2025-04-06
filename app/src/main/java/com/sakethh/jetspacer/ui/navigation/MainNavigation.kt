package com.sakethh.jetspacer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sakethh.jetspacer.ui.LocalNavController
import com.sakethh.jetspacer.ui.collection.CollectionsScreen
import com.sakethh.jetspacer.ui.explore.ExploreScreen
import com.sakethh.jetspacer.ui.explore.apodArchive.APODArchiveScreen
import com.sakethh.jetspacer.ui.explore.marsGallery.MarsGalleryScreen
import com.sakethh.jetspacer.ui.explore.search.SearchResultScreen
import com.sakethh.jetspacer.ui.headlines.TopHeadlineDetailScreen
import com.sakethh.jetspacer.ui.headlines.TopHeadlinesScreen
import com.sakethh.jetspacer.ui.home.HomeScreen
import com.sakethh.jetspacer.ui.home.settings.SettingsScreen

@Composable
fun MainNavigation() {
    val navController = LocalNavController.current
    NavHost(
        navController = navController, startDestination = JetSpacerNavigation.Root.Latest
    ) {
        composable<JetSpacerNavigation.Root.Latest> {
            HomeScreen(navController)
        }
        composable<JetSpacerNavigation.Root.Headlines> {
            TopHeadlinesScreen(navController)
        }
        composable<JetSpacerNavigation.Headlines.TopHeadlineDetailScreenRoute> { navBackStackEntry ->
            TopHeadlineDetailScreen(
                navBackStackEntry.toRoute<JetSpacerNavigation.Headlines.TopHeadlineDetailScreenRoute>().encodedString
            )
        }
        composable<JetSpacerNavigation.Root.Explore> {
            ExploreScreen(navController)
        }
        composable<JetSpacerNavigation.Explore.SearchResultScreenRoute> {
            SearchResultScreen(
                it.toRoute<JetSpacerNavigation.Explore.SearchResultScreenRoute>().encodedString
            )
        }
        composable<JetSpacerNavigation.Explore.APODArchiveScreen> {
            APODArchiveScreen(navController)
        }
        composable<JetSpacerNavigation.Explore.MarsGalleryRoute> {
            MarsGalleryScreen(navController)
        }
        composable<JetSpacerNavigation.Latest.Settings> {
            SettingsScreen(navController)
        }
        composable<JetSpacerNavigation.Root.Collections> {
            CollectionsScreen(navController)
        }
    }
}