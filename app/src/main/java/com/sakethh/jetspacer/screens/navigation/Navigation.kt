package com.sakethh.jetspacer.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sakethh.jetspacer.screens.bookMarks.BookMarksScreen
import com.sakethh.jetspacer.screens.home.HomeScreen
import com.sakethh.jetspacer.screens.space.SpaceScreen

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationRoutes.HOME_SCREEN) {
        composable(route = NavigationRoutes.HOME_SCREEN) {
            HomeScreen()
        }
        composable(route = NavigationRoutes.SPACE_SCREEN) {
            SpaceScreen(navController = navController)
        }
        composable(route = NavigationRoutes.BOOKMARKS_SCREEN) {
            BookMarksScreen(navController = navController)
        }
    }
}