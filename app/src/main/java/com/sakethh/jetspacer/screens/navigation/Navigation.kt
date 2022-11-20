package com.sakethh.jetspacer.screens.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.sakethh.jetspacer.screens.bookMarks.BookMarksScreen
import com.sakethh.jetspacer.screens.home.HomeScreen

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationRoutes.HOME_SCREEN) {
        composable(route = NavigationRoutes.HOME_SCREEN) {
            HomeScreen()
        }
        composable(route = NavigationRoutes.BOOKMARKS_SCREEN) {
            BookMarksScreen()
        }
    }
}