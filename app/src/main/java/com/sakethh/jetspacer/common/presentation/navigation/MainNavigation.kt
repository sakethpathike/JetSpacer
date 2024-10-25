package com.sakethh.jetspacer.common.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sakethh.jetspacer.home.presentation.HomeScreen
import com.sakethh.jetspacer.news.presentation.TopHeadlineDetailScreen
import com.sakethh.jetspacer.news.presentation.TopHeadlinesScreen
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeScreenRoute
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
    }
}

@Serializable
data object HomeScreenRoute

@Serializable
data object TopHeadlinesScreenRoute

@Serializable
data class TopHeadlineDetailScreenRoute(
    val encodedString: String
)