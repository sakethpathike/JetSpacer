package com.sakethh.jetspacer.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sakethh.jetspacer.screens.bookMarks.BookMarksScreen
import com.sakethh.jetspacer.screens.bookMarks.screens.SelectedBookMarkScreen
import com.sakethh.jetspacer.screens.home.HomeScreen
import com.sakethh.jetspacer.screens.news.NewsScreen
import com.sakethh.jetspacer.screens.settings.SettingsScreen
import com.sakethh.jetspacer.screens.space.SpaceScreen
import com.sakethh.jetspacer.screens.space.apod.APODScreen
import com.sakethh.jetspacer.screens.space.rovers.RoversScreen
import com.sakethh.jetspacer.screens.webview.WebViewScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainNavigation(navController: NavHostController, dataStore: DataStore<Preferences>) {
    NavHost(navController = navController, startDestination = NavigationRoutes.HOME_SCREEN) {
        composable(route = NavigationRoutes.HOME_SCREEN) {
            HomeScreen(navController = navController)
        }
        composable(route = NavigationRoutes.SPACE_SCREEN) {
            SpaceScreen(navController = navController)
        }
        composable(route = NavigationRoutes.BOOKMARKS_SCREEN) {
            BookMarksScreen(navController = navController)
        }
        composable(route = NavigationRoutes.APOD_SCREEN) {
            APODScreen(navController = navController)
        }
        composable(route = NavigationRoutes.ROVERS_SCREEN) {
            RoversScreen(navController = navController)
        }
        composable(route = NavigationRoutes.NEWS_SCREEN){
            NewsScreen(navController = navController)
        }
        composable(route = NavigationRoutes.SETTINGS_SCREEN){
            SettingsScreen(navController = navController,  dataStore = dataStore)
        }
        composable(route = NavigationRoutes.WEB_VIEW_SCREEN){
            WebViewScreen(navController = navController)
        }
        composable(route = NavigationRoutes.SELECTED_BOOKMARKS_SCREEN){
            SelectedBookMarkScreen(navController = navController)
        }
    }
}