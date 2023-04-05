package com.sakethh.jetspacer.screens.bookMarks.screens

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.sakethh.jetspacer.navigation.NavigationRoutes

@Composable
fun SelectedBookMarkScreen(navController: NavController) {
    BackHandler {
        navController.navigate(NavigationRoutes.BOOKMARKS_SCREEN){
            popUpTo(0)
        }
    }

}