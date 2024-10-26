package com.sakethh.jetspacer.common.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sakethh.jetspacer.explore.apodArchive.presentation.APODArchiveScreen
import com.sakethh.jetspacer.explore.presentation.ExploreScreen
import com.sakethh.jetspacer.explore.presentation.search.SearchResultScreen
import com.sakethh.jetspacer.home.presentation.HomeScreen
import com.sakethh.jetspacer.news.presentation.TopHeadlineDetailScreen
import com.sakethh.jetspacer.news.presentation.TopHeadlinesScreen
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ExploreScreenRoute
    ) {
        composable<HomeScreenRoute> {
            HomeScreen()
        }
        composable<TopHeadlinesScreenRoute> {
            TopHeadlinesScreen(navController)
        }
        composable<TopHeadlineDetailScreenRoute> { navBackStackEntry ->
            TopHeadlineDetailScreen(
                navBackStackEntry.toRoute<TopHeadlineDetailScreenRoute>().encodedString
            )
        }
        composable<ExploreScreenRoute> {
            ExploreScreen(navController)
        }
        composable<SearchResultScreenRoute> {
            SearchResultScreen(
                it.toRoute<SearchResultScreenRoute>().encodedString
            )
        }
        composable<APODArchiveScreen> {
            APODArchiveScreen(navController)
        }
    }
}

@Serializable
data object HomeScreenRoute

@Serializable
data object TopHeadlinesScreenRoute

@Serializable
data object ExploreScreenRoute

@Serializable
data class TopHeadlineDetailScreenRoute(
    val encodedString: String
)

@Serializable
data class SearchResultScreenRoute(
    val encodedString: String
)

@Serializable
data object APODArchiveScreen