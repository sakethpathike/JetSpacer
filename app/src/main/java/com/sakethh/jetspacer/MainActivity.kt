package com.sakethh.jetspacer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.sakethh.jetspacer.screens.HTTPClient
import com.sakethh.jetspacer.screens.bookMarks.BookMarksScreen
import com.sakethh.jetspacer.screens.home.HomeScreen
import com.sakethh.jetspacer.screens.navigation.BottomNavigationComposable
import com.sakethh.jetspacer.screens.navigation.MainNavigation
import com.sakethh.jetspacer.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController= rememberNavController()
                Scaffold(bottomBar = {
                    BottomNavigationComposable(
                        navController =navController
                    )
                }) {
                    MainNavigation(navController = navController)
                }
            }
        }
    }
}