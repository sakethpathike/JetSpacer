package com.sakethh.jetspacer.common.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sakethh.jetspacer.LocalNavController
import com.sakethh.jetspacer.collection.presentation.CollectionsScreen
import com.sakethh.jetspacer.explore.apodArchive.presentation.APODArchiveScreen
import com.sakethh.jetspacer.explore.marsGallery.presentation.MarsGalleryScreen
import com.sakethh.jetspacer.explore.presentation.ExploreScreen
import com.sakethh.jetspacer.explore.presentation.search.SearchResultScreen
import com.sakethh.jetspacer.headlines.presentation.TopHeadlineDetailScreen
import com.sakethh.jetspacer.headlines.presentation.TopHeadlinesScreen
import com.sakethh.jetspacer.home.presentation.HomeScreen
import com.sakethh.jetspacer.home.settings.presentation.SettingsScreen

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