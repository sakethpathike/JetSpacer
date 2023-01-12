package com.sakethh.jetspacer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.sakethh.jetspacer.navigation.BottomNavigationComposable
import com.sakethh.jetspacer.navigation.MainNavigation
import com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.ManifestForCuriosityVM
import com.sakethh.jetspacer.screens.space.rovers.opportunity.cameras.fhaz.FHAZOpportunityCameraScreen
import com.sakethh.jetspacer.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            ManifestForCuriosityVM().maxCuriositySol()
        }
        setContent {
            val navController = rememberNavController()
            AppTheme {
                Scaffold(
                    bottomBar = {
                        BottomNavigationComposable(
                            navController = navController
                        )
                    }) {
                    MainNavigation(
                        navController = navController
                    )
                }
            }
        }
    }
}
