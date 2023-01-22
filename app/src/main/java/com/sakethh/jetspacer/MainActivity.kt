package com.sakethh.jetspacer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.sakethh.jetspacer.navigation.BottomNavigationComposable
import com.sakethh.jetspacer.navigation.MainNavigation
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.ManifestForCuriosityVM
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            ManifestForCuriosityVM().maxCuriositySol()
        }
        GlobalScope.launch {
            HomeScreenViewModel.Network.isConnectedToInternet()
        }
        setContent {
            val navController = rememberNavController()
            val isConnectedToInternet =
                HomeScreenViewModel.Network.connectedToInternet.collectAsState()
            AppTheme {
                Scaffold(
                    bottomBar = {
                        BottomNavigationComposable(
                            navController = navController
                        )
                    }, floatingActionButton = {
                        if (!isConnectedToInternet.value || !HomeScreenViewModel.Network.isConnectionSucceed.value) {
                            Snackbar(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .padding(start = 20.dp, end = 20.dp, bottom = 50.dp)
                                    .wrapContentHeight()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(15.dp)
                            ) {
                                Text(
                                    text = "Network not detected, check your network settings",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    softWrap = true,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }, floatingActionButtonPosition = FabPosition.Center
                ) {
                    MainNavigation(
                        navController = navController
                    )
                }
            }
        }
    }
}
