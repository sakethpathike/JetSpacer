package com.sakethh.jetspacer.common.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sakethh.jetspacer.explore.presentation.ExploreScreen
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
            ExploreScreen()
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